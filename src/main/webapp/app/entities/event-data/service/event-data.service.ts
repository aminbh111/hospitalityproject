import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventData, getEventDataIdentifier } from '../event-data.model';

export type EntityResponseType = HttpResponse<IEventData>;
export type EntityArrayResponseType = HttpResponse<IEventData[]>;

@Injectable({ providedIn: 'root' })
export class EventDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/event-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eventData: IEventData): Observable<EntityResponseType> {
    return this.http.post<IEventData>(this.resourceUrl, eventData, { observe: 'response' });
  }

  update(eventData: IEventData): Observable<EntityResponseType> {
    return this.http.put<IEventData>(`${this.resourceUrl}/${getEventDataIdentifier(eventData) as number}`, eventData, {
      observe: 'response',
    });
  }

  partialUpdate(eventData: IEventData): Observable<EntityResponseType> {
    return this.http.patch<IEventData>(`${this.resourceUrl}/${getEventDataIdentifier(eventData) as number}`, eventData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEventData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEventData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEventDataToCollectionIfMissing(
    eventDataCollection: IEventData[],
    ...eventDataToCheck: (IEventData | null | undefined)[]
  ): IEventData[] {
    const eventData: IEventData[] = eventDataToCheck.filter(isPresent);
    if (eventData.length > 0) {
      const eventDataCollectionIdentifiers = eventDataCollection.map(eventDataItem => getEventDataIdentifier(eventDataItem)!);
      const eventDataToAdd = eventData.filter(eventDataItem => {
        const eventDataIdentifier = getEventDataIdentifier(eventDataItem);
        if (eventDataIdentifier == null || eventDataCollectionIdentifiers.includes(eventDataIdentifier)) {
          return false;
        }
        eventDataCollectionIdentifiers.push(eventDataIdentifier);
        return true;
      });
      return [...eventDataToAdd, ...eventDataCollection];
    }
    return eventDataCollection;
  }
}
