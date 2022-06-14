import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBars, Bars } from '../bars.model';
import { BarsService } from '../service/bars.service';
import { Position } from 'app/entities/enumerations/position.model';

@Component({
  selector: 'jhi-bars-update',
  templateUrl: './bars-update.component.html',
})
export class BarsUpdateComponent implements OnInit {
  isSaving = false;
  positionValues = Object.keys(Position);

  editForm = this.fb.group({
    id: [],
    date: [],
    publish: [],
    contentPosition: [],
    imagePosition: [],
  });

  constructor(protected barsService: BarsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bars }) => {
      if (bars.id === undefined) {
        const today = dayjs().startOf('day');
        bars.date = today;
      }

      this.updateForm(bars);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bars = this.createFromForm();
    if (bars.id !== undefined) {
      this.subscribeToSaveResponse(this.barsService.update(bars));
    } else {
      this.subscribeToSaveResponse(this.barsService.create(bars));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBars>>): void {
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

  protected updateForm(bars: IBars): void {
    this.editForm.patchValue({
      id: bars.id,
      date: bars.date ? bars.date.format(DATE_TIME_FORMAT) : null,
      publish: bars.publish,
      contentPosition: bars.contentPosition,
      imagePosition: bars.imagePosition,
    });
  }

  protected createFromForm(): IBars {
    return {
      ...new Bars(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      publish: this.editForm.get(['publish'])!.value,
      contentPosition: this.editForm.get(['contentPosition'])!.value,
      imagePosition: this.editForm.get(['imagePosition'])!.value,
    };
  }
}
