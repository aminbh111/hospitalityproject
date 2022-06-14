import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IConcierge, Concierge } from '../concierge.model';
import { ConciergeService } from '../service/concierge.service';
import { Position } from 'app/entities/enumerations/position.model';

@Component({
  selector: 'jhi-concierge-update',
  templateUrl: './concierge-update.component.html',
})
export class ConciergeUpdateComponent implements OnInit {
  isSaving = false;
  positionValues = Object.keys(Position);

  editForm = this.fb.group({
    id: [],
    date: [],
    publish: [],
    contentPosition: [],
    imagePosition: [],
  });

  constructor(protected conciergeService: ConciergeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concierge }) => {
      if (concierge.id === undefined) {
        const today = dayjs().startOf('day');
        concierge.date = today;
      }

      this.updateForm(concierge);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const concierge = this.createFromForm();
    if (concierge.id !== undefined) {
      this.subscribeToSaveResponse(this.conciergeService.update(concierge));
    } else {
      this.subscribeToSaveResponse(this.conciergeService.create(concierge));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConcierge>>): void {
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

  protected updateForm(concierge: IConcierge): void {
    this.editForm.patchValue({
      id: concierge.id,
      date: concierge.date ? concierge.date.format(DATE_TIME_FORMAT) : null,
      publish: concierge.publish,
      contentPosition: concierge.contentPosition,
      imagePosition: concierge.imagePosition,
    });
  }

  protected createFromForm(): IConcierge {
    return {
      ...new Concierge(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      publish: this.editForm.get(['publish'])!.value,
      contentPosition: this.editForm.get(['contentPosition'])!.value,
      imagePosition: this.editForm.get(['imagePosition'])!.value,
    };
  }
}
