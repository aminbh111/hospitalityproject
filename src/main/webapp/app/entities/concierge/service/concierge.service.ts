import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConcierge, getConciergeIdentifier } from '../concierge.model';

export type EntityResponseType = HttpResponse<IConcierge>;
export type EntityArrayResponseType = HttpResponse<IConcierge[]>;

@Injectable({ providedIn: 'root' })
export class ConciergeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/concierges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(concierge: IConcierge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(concierge);
    return this.http
      .post<IConcierge>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(concierge: IConcierge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(concierge);
    return this.http
      .put<IConcierge>(`${this.resourceUrl}/${getConciergeIdentifier(concierge) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(concierge: IConcierge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(concierge);
    return this.http
      .patch<IConcierge>(`${this.resourceUrl}/${getConciergeIdentifier(concierge) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IConcierge>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IConcierge[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConciergeToCollectionIfMissing(
    conciergeCollection: IConcierge[],
    ...conciergesToCheck: (IConcierge | null | undefined)[]
  ): IConcierge[] {
    const concierges: IConcierge[] = conciergesToCheck.filter(isPresent);
    if (concierges.length > 0) {
      const conciergeCollectionIdentifiers = conciergeCollection.map(conciergeItem => getConciergeIdentifier(conciergeItem)!);
      const conciergesToAdd = concierges.filter(conciergeItem => {
        const conciergeIdentifier = getConciergeIdentifier(conciergeItem);
        if (conciergeIdentifier == null || conciergeCollectionIdentifiers.includes(conciergeIdentifier)) {
          return false;
        }
        conciergeCollectionIdentifiers.push(conciergeIdentifier);
        return true;
      });
      return [...conciergesToAdd, ...conciergeCollection];
    }
    return conciergeCollection;
  }

  protected convertDateFromClient(concierge: IConcierge): IConcierge {
    return Object.assign({}, concierge, {
      date: concierge.date?.isValid() ? concierge.date.toJSON() : undefined,
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
      res.body.forEach((concierge: IConcierge) => {
        concierge.date = concierge.date ? dayjs(concierge.date) : undefined;
      });
    }
    return res;
  }
}
