import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoomService, getRoomServiceIdentifier } from '../room-service.model';

export type EntityResponseType = HttpResponse<IRoomService>;
export type EntityArrayResponseType = HttpResponse<IRoomService[]>;

@Injectable({ providedIn: 'root' })
export class RoomServiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/room-services');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(roomService: IRoomService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomService);
    return this.http
      .post<IRoomService>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(roomService: IRoomService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomService);
    return this.http
      .put<IRoomService>(`${this.resourceUrl}/${getRoomServiceIdentifier(roomService) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(roomService: IRoomService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomService);
    return this.http
      .patch<IRoomService>(`${this.resourceUrl}/${getRoomServiceIdentifier(roomService) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRoomService>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRoomService[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRoomServiceToCollectionIfMissing(
    roomServiceCollection: IRoomService[],
    ...roomServicesToCheck: (IRoomService | null | undefined)[]
  ): IRoomService[] {
    const roomServices: IRoomService[] = roomServicesToCheck.filter(isPresent);
    if (roomServices.length > 0) {
      const roomServiceCollectionIdentifiers = roomServiceCollection.map(roomServiceItem => getRoomServiceIdentifier(roomServiceItem)!);
      const roomServicesToAdd = roomServices.filter(roomServiceItem => {
        const roomServiceIdentifier = getRoomServiceIdentifier(roomServiceItem);
        if (roomServiceIdentifier == null || roomServiceCollectionIdentifiers.includes(roomServiceIdentifier)) {
          return false;
        }
        roomServiceCollectionIdentifiers.push(roomServiceIdentifier);
        return true;
      });
      return [...roomServicesToAdd, ...roomServiceCollection];
    }
    return roomServiceCollection;
  }

  protected convertDateFromClient(roomService: IRoomService): IRoomService {
    return Object.assign({}, roomService, {
      date: roomService.date?.isValid() ? roomService.date.toJSON() : undefined,
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
      res.body.forEach((roomService: IRoomService) => {
        roomService.date = roomService.date ? dayjs(roomService.date) : undefined;
      });
    }
    return res;
  }
}
