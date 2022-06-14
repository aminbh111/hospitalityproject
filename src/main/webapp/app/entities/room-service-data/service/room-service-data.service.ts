import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoomServiceData, getRoomServiceDataIdentifier } from '../room-service-data.model';

export type EntityResponseType = HttpResponse<IRoomServiceData>;
export type EntityArrayResponseType = HttpResponse<IRoomServiceData[]>;

@Injectable({ providedIn: 'root' })
export class RoomServiceDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/room-service-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(roomServiceData: IRoomServiceData): Observable<EntityResponseType> {
    return this.http.post<IRoomServiceData>(this.resourceUrl, roomServiceData, { observe: 'response' });
  }

  update(roomServiceData: IRoomServiceData): Observable<EntityResponseType> {
    return this.http.put<IRoomServiceData>(
      `${this.resourceUrl}/${getRoomServiceDataIdentifier(roomServiceData) as number}`,
      roomServiceData,
      { observe: 'response' }
    );
  }

  partialUpdate(roomServiceData: IRoomServiceData): Observable<EntityResponseType> {
    return this.http.patch<IRoomServiceData>(
      `${this.resourceUrl}/${getRoomServiceDataIdentifier(roomServiceData) as number}`,
      roomServiceData,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoomServiceData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoomServiceData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRoomServiceDataToCollectionIfMissing(
    roomServiceDataCollection: IRoomServiceData[],
    ...roomServiceDataToCheck: (IRoomServiceData | null | undefined)[]
  ): IRoomServiceData[] {
    const roomServiceData: IRoomServiceData[] = roomServiceDataToCheck.filter(isPresent);
    if (roomServiceData.length > 0) {
      const roomServiceDataCollectionIdentifiers = roomServiceDataCollection.map(
        roomServiceDataItem => getRoomServiceDataIdentifier(roomServiceDataItem)!
      );
      const roomServiceDataToAdd = roomServiceData.filter(roomServiceDataItem => {
        const roomServiceDataIdentifier = getRoomServiceDataIdentifier(roomServiceDataItem);
        if (roomServiceDataIdentifier == null || roomServiceDataCollectionIdentifiers.includes(roomServiceDataIdentifier)) {
          return false;
        }
        roomServiceDataCollectionIdentifiers.push(roomServiceDataIdentifier);
        return true;
      });
      return [...roomServiceDataToAdd, ...roomServiceDataCollection];
    }
    return roomServiceDataCollection;
  }
}
