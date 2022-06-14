import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAboutUsData, getAboutUsDataIdentifier } from '../about-us-data.model';

export type EntityResponseType = HttpResponse<IAboutUsData>;
export type EntityArrayResponseType = HttpResponse<IAboutUsData[]>;

@Injectable({ providedIn: 'root' })
export class AboutUsDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/about-us-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aboutUsData: IAboutUsData): Observable<EntityResponseType> {
    return this.http.post<IAboutUsData>(this.resourceUrl, aboutUsData, { observe: 'response' });
  }

  update(aboutUsData: IAboutUsData): Observable<EntityResponseType> {
    return this.http.put<IAboutUsData>(`${this.resourceUrl}/${getAboutUsDataIdentifier(aboutUsData) as number}`, aboutUsData, {
      observe: 'response',
    });
  }

  partialUpdate(aboutUsData: IAboutUsData): Observable<EntityResponseType> {
    return this.http.patch<IAboutUsData>(`${this.resourceUrl}/${getAboutUsDataIdentifier(aboutUsData) as number}`, aboutUsData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAboutUsData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAboutUsData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAboutUsDataToCollectionIfMissing(
    aboutUsDataCollection: IAboutUsData[],
    ...aboutUsDataToCheck: (IAboutUsData | null | undefined)[]
  ): IAboutUsData[] {
    const aboutUsData: IAboutUsData[] = aboutUsDataToCheck.filter(isPresent);
    if (aboutUsData.length > 0) {
      const aboutUsDataCollectionIdentifiers = aboutUsDataCollection.map(aboutUsDataItem => getAboutUsDataIdentifier(aboutUsDataItem)!);
      const aboutUsDataToAdd = aboutUsData.filter(aboutUsDataItem => {
        const aboutUsDataIdentifier = getAboutUsDataIdentifier(aboutUsDataItem);
        if (aboutUsDataIdentifier == null || aboutUsDataCollectionIdentifiers.includes(aboutUsDataIdentifier)) {
          return false;
        }
        aboutUsDataCollectionIdentifiers.push(aboutUsDataIdentifier);
        return true;
      });
      return [...aboutUsDataToAdd, ...aboutUsDataCollection];
    }
    return aboutUsDataCollection;
  }
}
