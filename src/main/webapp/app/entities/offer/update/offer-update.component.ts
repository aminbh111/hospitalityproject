import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOffer, Offer } from '../offer.model';
import { OfferService } from '../service/offer.service';
import { Position } from 'app/entities/enumerations/position.model';

@Component({
  selector: 'jhi-offer-update',
  templateUrl: './offer-update.component.html',
})
export class OfferUpdateComponent implements OnInit {
  isSaving = false;
  positionValues = Object.keys(Position);

  editForm = this.fb.group({
    id: [],
    date: [],
    publish: [],
    contentPosition: [],
    imagePosition: [],
  });

  constructor(protected offerService: OfferService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offer }) => {
      if (offer.id === undefined) {
        const today = dayjs().startOf('day');
        offer.date = today;
      }

      this.updateForm(offer);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const offer = this.createFromForm();
    if (offer.id !== undefined) {
      this.subscribeToSaveResponse(this.offerService.update(offer));
    } else {
      this.subscribeToSaveResponse(this.offerService.create(offer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOffer>>): void {
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

  protected updateForm(offer: IOffer): void {
    this.editForm.patchValue({
      id: offer.id,
      date: offer.date ? offer.date.format(DATE_TIME_FORMAT) : null,
      publish: offer.publish,
      contentPosition: offer.contentPosition,
      imagePosition: offer.imagePosition,
    });
  }

  protected createFromForm(): IOffer {
    return {
      ...new Offer(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      publish: this.editForm.get(['publish'])!.value,
      contentPosition: this.editForm.get(['contentPosition'])!.value,
      imagePosition: this.editForm.get(['imagePosition'])!.value,
    };
  }
}
