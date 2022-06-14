import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAboutUs, getAboutUsIdentifier } from '../about-us.model';

export type EntityResponseType = HttpResponse<IAboutUs>;
export type EntityArrayResponseType = HttpResponse<IAboutUs[]>;

@Injectable({ providedIn: 'root' })
export class AboutUsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aboutuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aboutUs: IAboutUs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aboutUs);
    return this.http
      .post<IAboutUs>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(aboutUs: IAboutUs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aboutUs);
    return this.http
      .put<IAboutUs>(`${this.resourceUrl}/${getAboutUsIdentifier(aboutUs) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(aboutUs: IAboutUs): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aboutUs);
    return this.http
      .patch<IAboutUs>(`${this.resourceUrl}/${getAboutUsIdentifier(aboutUs) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAboutUs>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAboutUs[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAboutUsToCollectionIfMissing(aboutUsCollection: IAboutUs[], ...aboutusesToCheck: (IAboutUs | null | undefined)[]): IAboutUs[] {
    const aboutuses: IAboutUs[] = aboutusesToCheck.filter(isPresent);
    if (aboutuses.length > 0) {
      const aboutUsCollectionIdentifiers = aboutUsCollection.map(aboutUsItem => getAboutUsIdentifier(aboutUsItem)!);
      const aboutusesToAdd = aboutuses.filter(aboutUsItem => {
        const aboutUsIdentifier = getAboutUsIdentifier(aboutUsItem);
        if (aboutUsIdentifier == null || aboutUsCollectionIdentifiers.includes(aboutUsIdentifier)) {
          return false;
        }
        aboutUsCollectionIdentifiers.push(aboutUsIdentifier);
        return true;
      });
      return [...aboutusesToAdd, ...aboutUsCollection];
    }
    return aboutUsCollection;
  }

  protected convertDateFromClient(aboutUs: IAboutUs): IAboutUs {
    return Object.assign({}, aboutUs, {
      date: aboutUs.date?.isValid() ? aboutUs.date.toJSON() : undefined,
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
      res.body.forEach((aboutUs: IAboutUs) => {
        aboutUs.date = aboutUs.date ? dayjs(aboutUs.date) : undefined;
      });
    }
    return res;
  }
}
