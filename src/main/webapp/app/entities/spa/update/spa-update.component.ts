import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISpa, Spa } from '../spa.model';
import { SpaService } from '../service/spa.service';
import { Position } from 'app/entities/enumerations/position.model';

@Component({
  selector: 'jhi-spa-update',
  templateUrl: './spa-update.component.html',
})
export class SpaUpdateComponent implements OnInit {
  isSaving = false;
  positionValues = Object.keys(Position);

  editForm = this.fb.group({
    id: [],
    date: [],
    publish: [],
    contentPosition: [],
    imagePosition: [],
  });

  constructor(protected spaService: SpaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spa }) => {
      if (spa.id === undefined) {
        const today = dayjs().startOf('day');
        spa.date = today;
      }

      this.updateForm(spa);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const spa = this.createFromForm();
    if (spa.id !== undefined) {
      this.subscribeToSaveResponse(this.spaService.update(spa));
    } else {
      this.subscribeToSaveResponse(this.spaService.create(spa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpa>>): void {
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

  protected updateForm(spa: ISpa): void {
    this.editForm.patchValue({
      id: spa.id,
      date: spa.date ? spa.date.format(DATE_TIME_FORMAT) : null,
      publish: spa.publish,
      contentPosition: spa.contentPosition,
      imagePosition: spa.imagePosition,
    });
  }

  protected createFromForm(): ISpa {
    return {
      ...new Spa(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      publish: this.editForm.get(['publish'])!.value,
      contentPosition: this.editForm.get(['contentPosition'])!.value,
      imagePosition: this.editForm.get(['imagePosition'])!.value,
    };
  }
}
