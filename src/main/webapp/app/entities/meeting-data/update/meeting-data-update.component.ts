import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMeetingData, MeetingData } from '../meeting-data.model';
import { MeetingDataService } from '../service/meeting-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMeeting } from 'app/entities/meeting/meeting.model';
import { MeetingService } from 'app/entities/meeting/service/meeting.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-meeting-data-update',
  templateUrl: './meeting-data-update.component.html',
})
export class MeetingDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  meetingsSharedCollection: IMeeting[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    meeting: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected meetingDataService: MeetingDataService,
    protected meetingService: MeetingService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meetingData }) => {
      this.updateForm(meetingData);

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
    const meetingData = this.createFromForm();
    if (meetingData.id !== undefined) {
      this.subscribeToSaveResponse(this.meetingDataService.update(meetingData));
    } else {
      this.subscribeToSaveResponse(this.meetingDataService.create(meetingData));
    }
  }

  trackMeetingById(_index: number, item: IMeeting): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeetingData>>): void {
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

  protected updateForm(meetingData: IMeetingData): void {
    this.editForm.patchValue({
      id: meetingData.id,
      lang: meetingData.lang,
      title: meetingData.title,
      content: meetingData.content,
      image: meetingData.image,
      imageContentType: meetingData.imageContentType,
      meeting: meetingData.meeting,
    });

    this.meetingsSharedCollection = this.meetingService.addMeetingToCollectionIfMissing(this.meetingsSharedCollection, meetingData.meeting);
  }

  protected loadRelationshipsOptions(): void {
    this.meetingService
      .query()
      .pipe(map((res: HttpResponse<IMeeting[]>) => res.body ?? []))
      .pipe(
        map((meetings: IMeeting[]) => this.meetingService.addMeetingToCollectionIfMissing(meetings, this.editForm.get('meeting')!.value))
      )
      .subscribe((meetings: IMeeting[]) => (this.meetingsSharedCollection = meetings));
  }

  protected createFromForm(): IMeetingData {
    return {
      ...new MeetingData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      meeting: this.editForm.get(['meeting'])!.value,
    };
  }
}
