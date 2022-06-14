import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBars, getBarsIdentifier } from '../bars.model';

export type EntityResponseType = HttpResponse<IBars>;
export type EntityArrayResponseType = HttpResponse<IBars[]>;

@Injectable({ providedIn: 'root' })
export class BarsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bars');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bars: IBars): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bars);
    return this.http
      .post<IBars>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bars: IBars): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bars);
    return this.http
      .put<IBars>(`${this.resourceUrl}/${getBarsIdentifier(bars) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(bars: IBars): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bars);
    return this.http
      .patch<IBars>(`${this.resourceUrl}/${getBarsIdentifier(bars) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBars>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBars[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBarsToCollectionIfMissing(barsCollection: IBars[], ...barsToCheck: (IBars | null | undefined)[]): IBars[] {
    const bars: IBars[] = barsToCheck.filter(isPresent);
    if (bars.length > 0) {
      const barsCollectionIdentifiers = barsCollection.map(barsItem => getBarsIdentifier(barsItem)!);
      const barsToAdd = bars.filter(barsItem => {
        const barsIdentifier = getBarsIdentifier(barsItem);
        if (barsIdentifier == null || barsCollectionIdentifiers.includes(barsIdentifier)) {
          return false;
        }
        barsCollectionIdentifiers.push(barsIdentifier);
        return true;
      });
      return [...barsToAdd, ...barsCollection];
    }
    return barsCollection;
  }

  protected convertDateFromClient(bars: IBars): IBars {
    return Object.assign({}, bars, {
      date: bars.date?.isValid() ? bars.date.toJSON() : undefined,
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
      res.body.forEach((bars: IBars) => {
        bars.date = bars.date ? dayjs(bars.date) : undefined;
      });
    }
    return res;
  }
}
