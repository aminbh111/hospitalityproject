import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IConciergeData, ConciergeData } from '../concierge-data.model';
import { ConciergeDataService } from '../service/concierge-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IConcierge } from 'app/entities/concierge/concierge.model';
import { ConciergeService } from 'app/entities/concierge/service/concierge.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-concierge-data-update',
  templateUrl: './concierge-data-update.component.html',
})
export class ConciergeDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  conciergesSharedCollection: IConcierge[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    concierge: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected conciergeDataService: ConciergeDataService,
    protected conciergeService: ConciergeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conciergeData }) => {
      this.updateForm(conciergeData);

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
    const conciergeData = this.createFromForm();
    if (conciergeData.id !== undefined) {
      this.subscribeToSaveResponse(this.conciergeDataService.update(conciergeData));
    } else {
      this.subscribeToSaveResponse(this.conciergeDataService.create(conciergeData));
    }
  }

  trackConciergeById(_index: number, item: IConcierge): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConciergeData>>): void {
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

  protected updateForm(conciergeData: IConciergeData): void {
    this.editForm.patchValue({
      id: conciergeData.id,
      lang: conciergeData.lang,
      title: conciergeData.title,
      content: conciergeData.content,
      image: conciergeData.image,
      imageContentType: conciergeData.imageContentType,
      concierge: conciergeData.concierge,
    });

    this.conciergesSharedCollection = this.conciergeService.addConciergeToCollectionIfMissing(
      this.conciergesSharedCollection,
      conciergeData.concierge
    );
  }

  protected loadRelationshipsOptions(): void {
    this.conciergeService
      .query()
      .pipe(map((res: HttpResponse<IConcierge[]>) => res.body ?? []))
      .pipe(
        map((concierges: IConcierge[]) =>
          this.conciergeService.addConciergeToCollectionIfMissing(concierges, this.editForm.get('concierge')!.value)
        )
      )
      .subscribe((concierges: IConcierge[]) => (this.conciergesSharedCollection = concierges));
  }

  protected createFromForm(): IConciergeData {
    return {
      ...new ConciergeData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      concierge: this.editForm.get(['concierge'])!.value,
    };
  }
}
