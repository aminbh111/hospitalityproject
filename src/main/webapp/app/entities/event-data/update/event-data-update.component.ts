import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEventData, EventData } from '../event-data.model';
import { EventDataService } from '../service/event-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-event-data-update',
  templateUrl: './event-data-update.component.html',
})
export class EventDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  eventsSharedCollection: IEvent[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    event: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected eventDataService: EventDataService,
    protected eventService: EventService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventData }) => {
      this.updateForm(eventData);

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
    const eventData = this.createFromForm();
    if (eventData.id !== undefined) {
      this.subscribeToSaveResponse(this.eventDataService.update(eventData));
    } else {
      this.subscribeToSaveResponse(this.eventDataService.create(eventData));
    }
  }

  trackEventById(_index: number, item: IEvent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventData>>): void {
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

  protected updateForm(eventData: IEventData): void {
    this.editForm.patchValue({
      id: eventData.id,
      lang: eventData.lang,
      title: eventData.title,
      content: eventData.content,
      image: eventData.image,
      imageContentType: eventData.imageContentType,
      event: eventData.event,
    });

    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing(this.eventsSharedCollection, eventData.event);
  }

  protected loadRelationshipsOptions(): void {
    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing(events, this.editForm.get('event')!.value)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));
  }

  protected createFromForm(): IEventData {
    return {
      ...new EventData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      event: this.editForm.get(['event'])!.value,
    };
  }
}
