import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IContactUs, ContactUs } from '../contact-us.model';
import { ContactUsService } from '../service/contact-us.service';
import { Position } from 'app/entities/enumerations/position.model';

@Component({
  selector: 'jhi-contact-us-update',
  templateUrl: './contact-us-update.component.html',
})
export class ContactUsUpdateComponent implements OnInit {
  isSaving = false;
  positionValues = Object.keys(Position);

  editForm = this.fb.group({
    id: [],
    date: [],
    publish: [],
    contentPosition: [],
    imagePosition: [],
  });

  constructor(protected contactUsService: ContactUsService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactUs }) => {
      if (contactUs.id === undefined) {
        const today = dayjs().startOf('day');
        contactUs.date = today;
      }

      this.updateForm(contactUs);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactUs = this.createFromForm();
    if (contactUs.id !== undefined) {
      this.subscribeToSaveResponse(this.contactUsService.update(contactUs));
    } else {
      this.subscribeToSaveResponse(this.contactUsService.create(contactUs));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactUs>>): void {
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

  protected updateForm(contactUs: IContactUs): void {
    this.editForm.patchValue({
      id: contactUs.id,
      date: contactUs.date ? contactUs.date.format(DATE_TIME_FORMAT) : null,
      publish: contactUs.publish,
      contentPosition: contactUs.contentPosition,
      imagePosition: contactUs.imagePosition,
    });
  }

  protected createFromForm(): IContactUs {
    return {
      ...new ContactUs(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      publish: this.editForm.get(['publish'])!.value,
      contentPosition: this.editForm.get(['contentPosition'])!.value,
      imagePosition: this.editForm.get(['imagePosition'])!.value,
    };
  }
}
