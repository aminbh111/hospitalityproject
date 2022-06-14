import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMeeting, Meeting } from '../meeting.model';
import { MeetingService } from '../service/meeting.service';
import { Position } from 'app/entities/enumerations/position.model';

@Component({
  selector: 'jhi-meeting-update',
  templateUrl: './meeting-update.component.html',
})
export class MeetingUpdateComponent implements OnInit {
  isSaving = false;
  positionValues = Object.keys(Position);

  editForm = this.fb.group({
    id: [],
    date: [],
    publish: [],
    contentPosition: [],
    imagePosition: [],
  });

  constructor(protected meetingService: MeetingService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meeting }) => {
      if (meeting.id === undefined) {
        const today = dayjs().startOf('day');
        meeting.date = today;
      }

      this.updateForm(meeting);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const meeting = this.createFromForm();
    if (meeting.id !== undefined) {
      this.subscribeToSaveResponse(this.meetingService.update(meeting));
    } else {
      this.subscribeToSaveResponse(this.meetingService.create(meeting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeeting>>): void {
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

  protected updateForm(meeting: IMeeting): void {
    this.editForm.patchValue({
      id: meeting.id,
      date: meeting.date ? meeting.date.format(DATE_TIME_FORMAT) : null,
      publish: meeting.publish,
      contentPosition: meeting.contentPosition,
      imagePosition: meeting.imagePosition,
    });
  }

  protected createFromForm(): IMeeting {
    return {
      ...new Meeting(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      publish: this.editForm.get(['publish'])!.value,
      contentPosition: this.editForm.get(['contentPosition'])!.value,
      imagePosition: this.editForm.get(['imagePosition'])!.value,
    };
  }
}
