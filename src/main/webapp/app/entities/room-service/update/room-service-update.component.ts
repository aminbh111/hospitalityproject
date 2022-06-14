import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRoomService, RoomService } from '../room-service.model';
import { RoomServiceService } from '../service/room-service.service';
import { Position } from 'app/entities/enumerations/position.model';

@Component({
  selector: 'jhi-room-service-update',
  templateUrl: './room-service-update.component.html',
})
export class RoomServiceUpdateComponent implements OnInit {
  isSaving = false;
  positionValues = Object.keys(Position);

  editForm = this.fb.group({
    id: [],
    date: [],
    publish: [],
    contentPosition: [],
    imagePosition: [],
  });

  constructor(protected roomServiceService: RoomServiceService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomService }) => {
      if (roomService.id === undefined) {
        const today = dayjs().startOf('day');
        roomService.date = today;
      }

      this.updateForm(roomService);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roomService = this.createFromForm();
    if (roomService.id !== undefined) {
      this.subscribeToSaveResponse(this.roomServiceService.update(roomService));
    } else {
      this.subscribeToSaveResponse(this.roomServiceService.create(roomService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomService>>): void {
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

  protected updateForm(roomService: IRoomService): void {
    this.editForm.patchValue({
      id: roomService.id,
      date: roomService.date ? roomService.date.format(DATE_TIME_FORMAT) : null,
      publish: roomService.publish,
      contentPosition: roomService.contentPosition,
      imagePosition: roomService.imagePosition,
    });
  }

  protected createFromForm(): IRoomService {
    return {
      ...new RoomService(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      publish: this.editForm.get(['publish'])!.value,
      contentPosition: this.editForm.get(['contentPosition'])!.value,
      imagePosition: this.editForm.get(['imagePosition'])!.value,
    };
  }
}
