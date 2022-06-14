import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IServiceData, ServiceData } from '../service-data.model';
import { ServiceDataService } from '../service/service-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IService } from 'app/entities/service/service.model';
import { ServiceService } from 'app/entities/service/service/service.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-service-data-update',
  templateUrl: './service-data-update.component.html',
})
export class ServiceDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  servicesSharedCollection: IService[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    service: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected serviceDataService: ServiceDataService,
    protected serviceService: ServiceService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceData }) => {
      this.updateForm(serviceData);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('hospitalityprojectApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceData = this.createFromForm();
    if (serviceData.id !== undefined) {
      this.subscribeToSaveResponse(this.serviceDataService.update(serviceData));
    } else {
      this.subscribeToSaveResponse(this.serviceDataService.create(serviceData));
    }
  }

  trackServiceById(_index: number, item: IService): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceData>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(serviceData: IServiceData): void {
    this.editForm.patchValue({
      id: serviceData.id,
      lang: serviceData.lang,
      title: serviceData.title,
      content: serviceData.content,
      image: serviceData.image,
      imageContentType: serviceData.imageContentType,
      service: serviceData.service,
    });

    this.servicesSharedCollection = this.serviceService.addServiceToCollectionIfMissing(this.servicesSharedCollection, serviceData.service);
  }

  protected loadRelationshipsOptions(): void {
    this.serviceService
      .query()
      .pipe(map((res: HttpResponse<IService[]>) => res.body ?? []))
      .pipe(
        map((services: IService[]) => this.serviceService.addServiceToCollectionIfMissing(services, this.editForm.get('service')!.value))
      )
      .subscribe((services: IService[]) => (this.servicesSharedCollection = services));
  }

  protected createFromForm(): IServiceData {
    return {
      ...new ServiceData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      service: this.editForm.get(['service'])!.value,
    };
  }
}
