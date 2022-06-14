import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISpaData, SpaData } from '../spa-data.model';
import { SpaDataService } from '../service/spa-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISpa } from 'app/entities/spa/spa.model';
import { SpaService } from 'app/entities/spa/service/spa.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-spa-data-update',
  templateUrl: './spa-data-update.component.html',
})
export class SpaDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  spasSharedCollection: ISpa[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    spa: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected spaDataService: SpaDataService,
    protected spaService: SpaService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spaData }) => {
      this.updateForm(spaData);

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
    const spaData = this.createFromForm();
    if (spaData.id !== undefined) {
      this.subscribeToSaveResponse(this.spaDataService.update(spaData));
    } else {
      this.subscribeToSaveResponse(this.spaDataService.create(spaData));
    }
  }

  trackSpaById(_index: number, item: ISpa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpaData>>): void {
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

  protected updateForm(spaData: ISpaData): void {
    this.editForm.patchValue({
      id: spaData.id,
      lang: spaData.lang,
      title: spaData.title,
      content: spaData.content,
      image: spaData.image,
      imageContentType: spaData.imageContentType,
      spa: spaData.spa,
    });

    this.spasSharedCollection = this.spaService.addSpaToCollectionIfMissing(this.spasSharedCollection, spaData.spa);
  }

  protected loadRelationshipsOptions(): void {
    this.spaService
      .query()
      .pipe(map((res: HttpResponse<ISpa[]>) => res.body ?? []))
      .pipe(map((spas: ISpa[]) => this.spaService.addSpaToCollectionIfMissing(spas, this.editForm.get('spa')!.value)))
      .subscribe((spas: ISpa[]) => (this.spasSharedCollection = spas));
  }

  protected createFromForm(): ISpaData {
    return {
      ...new SpaData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      spa: this.editForm.get(['spa'])!.value,
    };
  }
}
