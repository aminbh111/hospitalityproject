import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactUsData, getContactUsDataIdentifier } from '../contact-us-data.model';

export type EntityResponseType = HttpResponse<IContactUsData>;
export type EntityArrayResponseType = HttpResponse<IContactUsData[]>;

@Injectable({ providedIn: 'root' })
export class ContactUsDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-us-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactUsData: IContactUsData): Observable<EntityResponseType> {
    return this.http.post<IContactUsData>(this.resourceUrl, contactUsData, { observe: 'response' });
  }

  update(contactUsData: IContactUsData): Observable<EntityResponseType> {
    return this.http.put<IContactUsData>(`${this.resourceUrl}/${getContactUsDataIdentifier(contactUsData) as number}`, contactUsData, {
      observe: 'response',
    });
  }

  partialUpdate(contactUsData: IContactUsData): Observable<EntityResponseType> {
    return this.http.patch<IContactUsData>(`${this.resourceUrl}/${getContactUsDataIdentifier(contactUsData) as number}`, contactUsData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactUsData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactUsData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactUsDataToCollectionIfMissing(
    contactUsDataCollection: IContactUsData[],
    ...contactUsDataToCheck: (IContactUsData | null | undefined)[]
  ): IContactUsData[] {
    const contactUsData: IContactUsData[] = contactUsDataToCheck.filter(isPresent);
    if (contactUsData.length > 0) {
      const contactUsDataCollectionIdentifiers = contactUsDataCollection.map(
        contactUsDataItem => getContactUsDataIdentifier(contactUsDataItem)!
      );
      const contactUsDataToAdd = contactUsData.filter(contactUsDataItem => {
        const contactUsDataIdentifier = getContactUsDataIdentifier(contactUsDataItem);
        if (contactUsDataIdentifier == null || contactUsDataCollectionIdentifiers.includes(contactUsDataIdentifier)) {
          return false;
        }
        contactUsDataCollectionIdentifiers.push(contactUsDataIdentifier);
        return true;
      });
      return [...contactUsDataToAdd, ...contactUsDataCollection];
    }
    return contactUsDataCollection;
  }
}
