import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAboutUs, AboutUs } from '../about-us.model';
import { AboutUsService } from '../service/about-us.service';
import { Position } from 'app/entities/enumerations/position.model';

@Component({
  selector: 'jhi-about-us-update',
  templateUrl: './about-us-update.component.html',
})
export class AboutUsUpdateComponent implements OnInit {
  isSaving = false;
  positionValues = Object.keys(Position);

  editForm = this.fb.group({
    id: [],
    date: [],
    publish: [],
    contentPosition: [],
    imagePosition: [],
  });

  constructor(protected aboutUsService: AboutUsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aboutUs }) => {
      if (aboutUs.id === undefined) {
        const today = dayjs().startOf('day');
        aboutUs.date = today;
      }

      this.updateForm(aboutUs);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aboutUs = this.createFromForm();
    if (aboutUs.id !== undefined) {
      this.subscribeToSaveResponse(this.aboutUsService.update(aboutUs));
    } else {
      this.subscribeToSaveResponse(this.aboutUsService.create(aboutUs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAboutUs>>): void {
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

  protected updateForm(aboutUs: IAboutUs): void {
    this.editForm.patchValue({
      id: aboutUs.id,
      date: aboutUs.date ? aboutUs.date.format(DATE_TIME_FORMAT) : null,
      publish: aboutUs.publish,
      contentPosition: aboutUs.contentPosition,
      imagePosition: aboutUs.imagePosition,
    });
  }

  protected createFromForm(): IAboutUs {
    return {
      ...new AboutUs(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      publish: this.editForm.get(['publish'])!.value,
      contentPosition: this.editForm.get(['contentPosition'])!.value,
      imagePosition: this.editForm.get(['imagePosition'])!.value,
    };
  }
}
