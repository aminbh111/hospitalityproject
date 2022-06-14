import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlaylist, getPlaylistIdentifier } from '../playlist.model';

export type EntityResponseType = HttpResponse<IPlaylist>;
export type EntityArrayResponseType = HttpResponse<IPlaylist[]>;

@Injectable({ providedIn: 'root' })
export class PlaylistService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/playlists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(playlist: IPlaylist): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playlist);
    return this.http
      .post<IPlaylist>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(playlist: IPlaylist): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playlist);
    return this.http
      .put<IPlaylist>(`${this.resourceUrl}/${getPlaylistIdentifier(playlist) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(playlist: IPlaylist): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playlist);
    return this.http
      .patch<IPlaylist>(`${this.resourceUrl}/${getPlaylistIdentifier(playlist) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPlaylist>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlaylist[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlaylistToCollectionIfMissing(playlistCollection: IPlaylist[], ...playlistsToCheck: (IPlaylist | null | undefined)[]): IPlaylist[] {
    const playlists: IPlaylist[] = playlistsToCheck.filter(isPresent);
    if (playlists.length > 0) {
      const playlistCollectionIdentifiers = playlistCollection.map(playlistItem => getPlaylistIdentifier(playlistItem)!);
      const playlistsToAdd = playlists.filter(playlistItem => {
        const playlistIdentifier = getPlaylistIdentifier(playlistItem);
        if (playlistIdentifier == null || playlistCollectionIdentifiers.includes(playlistIdentifier)) {
          return false;
        }
        playlistCollectionIdentifiers.push(playlistIdentifier);
        return true;
      });
      return [...playlistsToAdd, ...playlistCollection];
    }
    return playlistCollection;
  }

  protected convertDateFromClient(playlist: IPlaylist): IPlaylist {
    return Object.assign({}, playlist, {
      date: playlist.date?.isValid() ? playlist.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((playlist: IPlaylist) => {
        playlist.date = playlist.date ? dayjs(playlist.date) : undefined;
      });
    }
    return res;
  }
}
