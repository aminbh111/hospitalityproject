import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOfferData, getOfferDataIdentifier } from '../offer-data.model';

export type EntityResponseType = HttpResponse<IOfferData>;
export type EntityArrayResponseType = HttpResponse<IOfferData[]>;

@Injectable({ providedIn: 'root' })
export class OfferDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/offer-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(offerData: IOfferData): Observable<EntityResponseType> {
    return this.http.post<IOfferData>(this.resourceUrl, offerData, { observe: 'response' });
  }

  update(offerData: IOfferData): Observable<EntityResponseType> {
    return this.http.put<IOfferData>(`${this.resourceUrl}/${getOfferDataIdentifier(offerData) as number}`, offerData, {
      observe: 'response',
    });
  }

  partialUpdate(offerData: IOfferData): Observable<EntityResponseType> {
    return this.http.patch<IOfferData>(`${this.resourceUrl}/${getOfferDataIdentifier(offerData) as number}`, offerData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOfferData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOfferData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOfferDataToCollectionIfMissing(
    offerDataCollection: IOfferData[],
    ...offerDataToCheck: (IOfferData | null | undefined)[]
  ): IOfferData[] {
    const offerData: IOfferData[] = offerDataToCheck.filter(isPresent);
    if (offerData.length > 0) {
      const offerDataCollectionIdentifiers = offerDataCollection.map(offerDataItem => getOfferDataIdentifier(offerDataItem)!);
      const offerDataToAdd = offerData.filter(offerDataItem => {
        const offerDataIdentifier = getOfferDataIdentifier(offerDataItem);
        if (offerDataIdentifier == null || offerDataCollectionIdentifiers.includes(offerDataIdentifier)) {
          return false;
        }
        offerDataCollectionIdentifiers.push(offerDataIdentifier);
        return true;
      });
      return [...offerDataToAdd, ...offerDataCollection];
    }
    return offerDataCollection;
  }
}
