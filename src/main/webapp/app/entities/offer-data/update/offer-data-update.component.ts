import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOfferData, OfferData } from '../offer-data.model';
import { OfferDataService } from '../service/offer-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-offer-data-update',
  templateUrl: './offer-data-update.component.html',
})
export class OfferDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  offersSharedCollection: IOffer[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    offer: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected offerDataService: OfferDataService,
    protected offerService: OfferService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offerData }) => {
      this.updateForm(offerData);

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
    const offerData = this.createFromForm();
    if (offerData.id !== undefined) {
      this.subscribeToSaveResponse(this.offerDataService.update(offerData));
    } else {
      this.subscribeToSaveResponse(this.offerDataService.create(offerData));
    }
  }

  trackOfferById(_index: number, item: IOffer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOfferData>>): void {
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

  protected updateForm(offerData: IOfferData): void {
    this.editForm.patchValue({
      id: offerData.id,
      lang: offerData.lang,
      title: offerData.title,
      content: offerData.content,
      image: offerData.image,
      imageContentType: offerData.imageContentType,
      offer: offerData.offer,
    });

    this.offersSharedCollection = this.offerService.addOfferToCollectionIfMissing(this.offersSharedCollection, offerData.offer);
  }

  protected loadRelationshipsOptions(): void {
    this.offerService
      .query()
      .pipe(map((res: HttpResponse<IOffer[]>) => res.body ?? []))
      .pipe(map((offers: IOffer[]) => this.offerService.addOfferToCollectionIfMissing(offers, this.editForm.get('offer')!.value)))
      .subscribe((offers: IOffer[]) => (this.offersSharedCollection = offers));
  }

  protected createFromForm(): IOfferData {
    return {
      ...new OfferData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      offer: this.editForm.get(['offer'])!.value,
    };
  }
}
