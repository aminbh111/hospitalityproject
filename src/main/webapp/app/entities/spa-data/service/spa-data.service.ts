import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpaData, getSpaDataIdentifier } from '../spa-data.model';

export type EntityResponseType = HttpResponse<ISpaData>;
export type EntityArrayResponseType = HttpResponse<ISpaData[]>;

@Injectable({ providedIn: 'root' })
export class SpaDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spa-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(spaData: ISpaData): Observable<EntityResponseType> {
    return this.http.post<ISpaData>(this.resourceUrl, spaData, { observe: 'response' });
  }

  update(spaData: ISpaData): Observable<EntityResponseType> {
    return this.http.put<ISpaData>(`${this.resourceUrl}/${getSpaDataIdentifier(spaData) as number}`, spaData, { observe: 'response' });
  }

  partialUpdate(spaData: ISpaData): Observable<EntityResponseType> {
    return this.http.patch<ISpaData>(`${this.resourceUrl}/${getSpaDataIdentifier(spaData) as number}`, spaData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpaData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpaData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSpaDataToCollectionIfMissing(spaDataCollection: ISpaData[], ...spaDataToCheck: (ISpaData | null | undefined)[]): ISpaData[] {
    const spaData: ISpaData[] = spaDataToCheck.filter(isPresent);
    if (spaData.length > 0) {
      const spaDataCollectionIdentifiers = spaDataCollection.map(spaDataItem => getSpaDataIdentifier(spaDataItem)!);
      const spaDataToAdd = spaData.filter(spaDataItem => {
        const spaDataIdentifier = getSpaDataIdentifier(spaDataItem);
        if (spaDataIdentifier == null || spaDataCollectionIdentifiers.includes(spaDataIdentifier)) {
          return false;
        }
        spaDataCollectionIdentifiers.push(spaDataIdentifier);
        return true;
      });
      return [...spaDataToAdd, ...spaDataCollection];
    }
    return spaDataCollection;
  }
}
