import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRoomServiceData, RoomServiceData } from '../room-service-data.model';
import { RoomServiceDataService } from '../service/room-service-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IRoomService } from 'app/entities/room-service/room-service.model';
import { RoomServiceService } from 'app/entities/room-service/service/room-service.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-room-service-data-update',
  templateUrl: './room-service-data-update.component.html',
})
export class RoomServiceDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  roomServicesSharedCollection: IRoomService[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    roomService: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected roomServiceDataService: RoomServiceDataService,
    protected roomServiceService: RoomServiceService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomServiceData }) => {
      this.updateForm(roomServiceData);

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
    const roomServiceData = this.createFromForm();
    if (roomServiceData.id !== undefined) {
      this.subscribeToSaveResponse(this.roomServiceDataService.update(roomServiceData));
    } else {
      this.subscribeToSaveResponse(this.roomServiceDataService.create(roomServiceData));
    }
  }

  trackRoomServiceById(_index: number, item: IRoomService): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomServiceData>>): void {
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

  protected updateForm(roomServiceData: IRoomServiceData): void {
    this.editForm.patchValue({
      id: roomServiceData.id,
      lang: roomServiceData.lang,
      title: roomServiceData.title,
      content: roomServiceData.content,
      image: roomServiceData.image,
      imageContentType: roomServiceData.imageContentType,
      roomService: roomServiceData.roomService,
    });

    this.roomServicesSharedCollection = this.roomServiceService.addRoomServiceToCollectionIfMissing(
      this.roomServicesSharedCollection,
      roomServiceData.roomService
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roomServiceService
      .query()
      .pipe(map((res: HttpResponse<IRoomService[]>) => res.body ?? []))
      .pipe(
        map((roomServices: IRoomService[]) =>
          this.roomServiceService.addRoomServiceToCollectionIfMissing(roomServices, this.editForm.get('roomService')!.value)
        )
      )
      .subscribe((roomServices: IRoomService[]) => (this.roomServicesSharedCollection = roomServices));
  }

  protected createFromForm(): IRoomServiceData {
    return {
      ...new RoomServiceData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      roomService: this.editForm.get(['roomService'])!.value,
    };
  }
}
