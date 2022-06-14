import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBarsData, getBarsDataIdentifier } from '../bars-data.model';

export type EntityResponseType = HttpResponse<IBarsData>;
export type EntityArrayResponseType = HttpResponse<IBarsData[]>;

@Injectable({ providedIn: 'root' })
export class BarsDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bars-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(barsData: IBarsData): Observable<EntityResponseType> {
    return this.http.post<IBarsData>(this.resourceUrl, barsData, { observe: 'response' });
  }

  update(barsData: IBarsData): Observable<EntityResponseType> {
    return this.http.put<IBarsData>(`${this.resourceUrl}/${getBarsDataIdentifier(barsData) as number}`, barsData, { observe: 'response' });
  }

  partialUpdate(barsData: IBarsData): Observable<EntityResponseType> {
    return this.http.patch<IBarsData>(`${this.resourceUrl}/${getBarsDataIdentifier(barsData) as number}`, barsData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBarsData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBarsData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBarsDataToCollectionIfMissing(barsDataCollection: IBarsData[], ...barsDataToCheck: (IBarsData | null | undefined)[]): IBarsData[] {
    const barsData: IBarsData[] = barsDataToCheck.filter(isPresent);
    if (barsData.length > 0) {
      const barsDataCollectionIdentifiers = barsDataCollection.map(barsDataItem => getBarsDataIdentifier(barsDataItem)!);
      const barsDataToAdd = barsData.filter(barsDataItem => {
        const barsDataIdentifier = getBarsDataIdentifier(barsDataItem);
        if (barsDataIdentifier == null || barsDataCollectionIdentifiers.includes(barsDataIdentifier)) {
          return false;
        }
        barsDataCollectionIdentifiers.push(barsDataIdentifier);
        return true;
      });
      return [...barsDataToAdd, ...barsDataCollection];
    }
    return barsDataCollection;
  }
}
