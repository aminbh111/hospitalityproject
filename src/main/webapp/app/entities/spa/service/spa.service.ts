import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpa, getSpaIdentifier } from '../spa.model';

export type EntityResponseType = HttpResponse<ISpa>;
export type EntityArrayResponseType = HttpResponse<ISpa[]>;

@Injectable({ providedIn: 'root' })
export class SpaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(spa: ISpa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spa);
    return this.http
      .post<ISpa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(spa: ISpa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spa);
    return this.http
      .put<ISpa>(`${this.resourceUrl}/${getSpaIdentifier(spa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(spa: ISpa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(spa);
    return this.http
      .patch<ISpa>(`${this.resourceUrl}/${getSpaIdentifier(spa) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISpa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISpa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSpaToCollectionIfMissing(spaCollection: ISpa[], ...spasToCheck: (ISpa | null | undefined)[]): ISpa[] {
    const spas: ISpa[] = spasToCheck.filter(isPresent);
    if (spas.length > 0) {
      const spaCollectionIdentifiers = spaCollection.map(spaItem => getSpaIdentifier(spaItem)!);
      const spasToAdd = spas.filter(spaItem => {
        const spaIdentifier = getSpaIdentifier(spaItem);
        if (spaIdentifier == null || spaCollectionIdentifiers.includes(spaIdentifier)) {
          return false;
        }
        spaCollectionIdentifiers.push(spaIdentifier);
        return true;
      });
      return [...spasToAdd, ...spaCollection];
    }
    return spaCollection;
  }

  protected convertDateFromClient(spa: ISpa): ISpa {
    return Object.assign({}, spa, {
      date: spa.date?.isValid() ? spa.date.toJSON() : undefined,
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
      res.body.forEach((spa: ISpa) => {
        spa.date = spa.date ? dayjs(spa.date) : undefined;
      });
    }
    return res;
  }
}
