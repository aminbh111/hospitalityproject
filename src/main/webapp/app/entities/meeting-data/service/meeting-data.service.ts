import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeetingData, getMeetingDataIdentifier } from '../meeting-data.model';

export type EntityResponseType = HttpResponse<IMeetingData>;
export type EntityArrayResponseType = HttpResponse<IMeetingData[]>;

@Injectable({ providedIn: 'root' })
export class MeetingDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meeting-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(meetingData: IMeetingData): Observable<EntityResponseType> {
    return this.http.post<IMeetingData>(this.resourceUrl, meetingData, { observe: 'response' });
  }

  update(meetingData: IMeetingData): Observable<EntityResponseType> {
    return this.http.put<IMeetingData>(`${this.resourceUrl}/${getMeetingDataIdentifier(meetingData) as number}`, meetingData, {
      observe: 'response',
    });
  }

  partialUpdate(meetingData: IMeetingData): Observable<EntityResponseType> {
    return this.http.patch<IMeetingData>(`${this.resourceUrl}/${getMeetingDataIdentifier(meetingData) as number}`, meetingData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMeetingData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMeetingData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMeetingDataToCollectionIfMissing(
    meetingDataCollection: IMeetingData[],
    ...meetingDataToCheck: (IMeetingData | null | undefined)[]
  ): IMeetingData[] {
    const meetingData: IMeetingData[] = meetingDataToCheck.filter(isPresent);
    if (meetingData.length > 0) {
      const meetingDataCollectionIdentifiers = meetingDataCollection.map(meetingDataItem => getMeetingDataIdentifier(meetingDataItem)!);
      const meetingDataToAdd = meetingData.filter(meetingDataItem => {
        const meetingDataIdentifier = getMeetingDataIdentifier(meetingDataItem);
        if (meetingDataIdentifier == null || meetingDataCollectionIdentifiers.includes(meetingDataIdentifier)) {
          return false;
        }
        meetingDataCollectionIdentifiers.push(meetingDataIdentifier);
        return true;
      });
      return [...meetingDataToAdd, ...meetingDataCollection];
    }
    return meetingDataCollection;
  }
}
