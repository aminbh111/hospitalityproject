import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactUs, getContactUsIdentifier } from '../contact-us.model';

export type EntityResponseType = HttpResponse<IContactUs>;
export type EntityArrayResponseType = HttpResponse<IContactUs[]>;

@Injectable({ providedIn: 'root' })
export class ContactUsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contactuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactUs: IContactUs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactUs);
    return this.http
      .post<IContactUs>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contactUs: IContactUs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactUs);
    return this.http
      .put<IContactUs>(`${this.resourceUrl}/${getContactUsIdentifier(contactUs) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contactUs: IContactUs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contactUs);
    return this.http
      .patch<IContactUs>(`${this.resourceUrl}/${getContactUsIdentifier(contactUs) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContactUs>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContactUs[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactUsToCollectionIfMissing(
    contactUsCollection: IContactUs[],
    ...contactusesToCheck: (IContactUs | null | undefined)[]
  ): IContactUs[] {
    const contactuses: IContactUs[] = contactusesToCheck.filter(isPresent);
    if (contactuses.length > 0) {
      const contactUsCollectionIdentifiers = contactUsCollection.map(contactUsItem => getContactUsIdentifier(contactUsItem)!);
      const contactusesToAdd = contactuses.filter(contactUsItem => {
        const contactUsIdentifier = getContactUsIdentifier(contactUsItem);
        if (contactUsIdentifier == null || contactUsCollectionIdentifiers.includes(contactUsIdentifier)) {
          return false;
        }
        contactUsCollectionIdentifiers.push(contactUsIdentifier);
        return true;
      });
      return [...contactusesToAdd, ...contactUsCollection];
    }
    return contactUsCollection;
  }

  protected convertDateFromClient(contactUs: IContactUs): IContactUs {
    return Object.assign({}, contactUs, {
      date: contactUs.date?.isValid() ? contactUs.date.toJSON() : undefined,
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
      res.body.forEach((contactUs: IContactUs) => {
        contactUs.date = contactUs.date ? dayjs(contactUs.date) : undefined;
      });
    }
    return res;
  }
}
