import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPlaylist, Playlist } from '../playlist.model';
import { PlaylistService } from '../service/playlist.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-playlist-update',
  templateUrl: './playlist-update.component.html',
})
export class PlaylistUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  editForm = this.fb.group({
    id: [],
    date: [],
    lang: [],
    file: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected playlistService: PlaylistService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playlist }) => {
      if (playlist.id === undefined) {
        const today = dayjs().startOf('day');
        playlist.date = today;
      }

      this.updateForm(playlist);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playlist = this.createFromForm();
    if (playlist.id !== undefined) {
      this.subscribeToSaveResponse(this.playlistService.update(playlist));
    } else {
      this.subscribeToSaveResponse(this.playlistService.create(playlist));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaylist>>): void {
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

  protected updateForm(playlist: IPlaylist): void {
    this.editForm.patchValue({
      id: playlist.id,
      date: playlist.date ? playlist.date.format(DATE_TIME_FORMAT) : null,
      lang: playlist.lang,
      file: playlist.file,
    });
  }

  protected createFromForm(): IPlaylist {
    return {
      ...new Playlist(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      lang: this.editForm.get(['lang'])!.value,
      file: this.editForm.get(['file'])!.value,
    };
  }
}
