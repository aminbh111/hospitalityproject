import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBarsData, BarsData } from '../bars-data.model';
import { BarsDataService } from '../service/bars-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IBars } from 'app/entities/bars/bars.model';
import { BarsService } from 'app/entities/bars/service/bars.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-bars-data-update',
  templateUrl: './bars-data-update.component.html',
})
export class BarsDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  barsSharedCollection: IBars[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    bars: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected barsDataService: BarsDataService,
    protected barsService: BarsService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ barsData }) => {
      this.updateForm(barsData);

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
    const barsData = this.createFromForm();
    if (barsData.id !== undefined) {
      this.subscribeToSaveResponse(this.barsDataService.update(barsData));
    } else {
      this.subscribeToSaveResponse(this.barsDataService.create(barsData));
    }
  }

  trackBarsById(_index: number, item: IBars): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBarsData>>): void {
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

  protected updateForm(barsData: IBarsData): void {
    this.editForm.patchValue({
      id: barsData.id,
      lang: barsData.lang,
      title: barsData.title,
      content: barsData.content,
      image: barsData.image,
      imageContentType: barsData.imageContentType,
      bars: barsData.bars,
    });

    this.barsSharedCollection = this.barsService.addBarsToCollectionIfMissing(this.barsSharedCollection, barsData.bars);
  }

  protected loadRelationshipsOptions(): void {
    this.barsService
      .query()
      .pipe(map((res: HttpResponse<IBars[]>) => res.body ?? []))
      .pipe(map((bars: IBars[]) => this.barsService.addBarsToCollectionIfMissing(bars, this.editForm.get('bars')!.value)))
      .subscribe((bars: IBars[]) => (this.barsSharedCollection = bars));
  }

  protected createFromForm(): IBarsData {
    return {
      ...new BarsData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      bars: this.editForm.get(['bars'])!.value,
    };
  }
}
