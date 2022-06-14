import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeeting, getMeetingIdentifier } from '../meeting.model';

export type EntityResponseType = HttpResponse<IMeeting>;
export type EntityArrayResponseType = HttpResponse<IMeeting[]>;

@Injectable({ providedIn: 'root' })
export class MeetingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meetings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(meeting: IMeeting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meeting);
    return this.http
      .post<IMeeting>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(meeting: IMeeting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meeting);
    return this.http
      .put<IMeeting>(`${this.resourceUrl}/${getMeetingIdentifier(meeting) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(meeting: IMeeting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meeting);
    return this.http
      .patch<IMeeting>(`${this.resourceUrl}/${getMeetingIdentifier(meeting) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMeeting>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMeeting[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMeetingToCollectionIfMissing(meetingCollection: IMeeting[], ...meetingsToCheck: (IMeeting | null | undefined)[]): IMeeting[] {
    const meetings: IMeeting[] = meetingsToCheck.filter(isPresent);
    if (meetings.length > 0) {
      const meetingCollectionIdentifiers = meetingCollection.map(meetingItem => getMeetingIdentifier(meetingItem)!);
      const meetingsToAdd = meetings.filter(meetingItem => {
        const meetingIdentifier = getMeetingIdentifier(meetingItem);
        if (meetingIdentifier == null || meetingCollectionIdentifiers.includes(meetingIdentifier)) {
          return false;
        }
        meetingCollectionIdentifiers.push(meetingIdentifier);
        return true;
      });
      return [...meetingsToAdd, ...meetingCollection];
    }
    return meetingCollection;
  }

  protected convertDateFromClient(meeting: IMeeting): IMeeting {
    return Object.assign({}, meeting, {
      date: meeting.date?.isValid() ? meeting.date.toJSON() : undefined,
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
      res.body.forEach((meeting: IMeeting) => {
        meeting.date = meeting.date ? dayjs(meeting.date) : undefined;
      });
    }
    return res;
  }
}
