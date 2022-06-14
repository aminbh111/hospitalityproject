import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOffer, getOfferIdentifier } from '../offer.model';

export type EntityResponseType = HttpResponse<IOffer>;
export type EntityArrayResponseType = HttpResponse<IOffer[]>;

@Injectable({ providedIn: 'root' })
export class OfferService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/offers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(offer: IOffer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(offer);
    return this.http
      .post<IOffer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(offer: IOffer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(offer);
    return this.http
      .put<IOffer>(`${this.resourceUrl}/${getOfferIdentifier(offer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(offer: IOffer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(offer);
    return this.http
      .patch<IOffer>(`${this.resourceUrl}/${getOfferIdentifier(offer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOffer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOffer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOfferToCollectionIfMissing(offerCollection: IOffer[], ...offersToCheck: (IOffer | null | undefined)[]): IOffer[] {
    const offers: IOffer[] = offersToCheck.filter(isPresent);
    if (offers.length > 0) {
      const offerCollectionIdentifiers = offerCollection.map(offerItem => getOfferIdentifier(offerItem)!);
      const offersToAdd = offers.filter(offerItem => {
        const offerIdentifier = getOfferIdentifier(offerItem);
        if (offerIdentifier == null || offerCollectionIdentifiers.includes(offerIdentifier)) {
          return false;
        }
        offerCollectionIdentifiers.push(offerIdentifier);
        return true;
      });
      return [...offersToAdd, ...offerCollection];
    }
    return offerCollection;
  }

  protected convertDateFromClient(offer: IOffer): IOffer {
    return Object.assign({}, offer, {
      date: offer.date?.isValid() ? offer.date.toJSON() : undefined,
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
      res.body.forEach((offer: IOffer) => {
        offer.date = offer.date ? dayjs(offer.date) : undefined;
      });
    }
    return res;
  }
}
