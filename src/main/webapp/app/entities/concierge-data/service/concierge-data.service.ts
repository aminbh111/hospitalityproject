import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConciergeData, getConciergeDataIdentifier } from '../concierge-data.model';

export type EntityResponseType = HttpResponse<IConciergeData>;
export type EntityArrayResponseType = HttpResponse<IConciergeData[]>;

@Injectable({ providedIn: 'root' })
export class ConciergeDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/concierge-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(conciergeData: IConciergeData): Observable<EntityResponseType> {
    return this.http.post<IConciergeData>(this.resourceUrl, conciergeData, { observe: 'response' });
  }

  update(conciergeData: IConciergeData): Observable<EntityResponseType> {
    return this.http.put<IConciergeData>(`${this.resourceUrl}/${getConciergeDataIdentifier(conciergeData) as number}`, conciergeData, {
      observe: 'response',
    });
  }

  partialUpdate(conciergeData: IConciergeData): Observable<EntityResponseType> {
    return this.http.patch<IConciergeData>(`${this.resourceUrl}/${getConciergeDataIdentifier(conciergeData) as number}`, conciergeData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConciergeData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConciergeData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConciergeDataToCollectionIfMissing(
    conciergeDataCollection: IConciergeData[],
    ...conciergeDataToCheck: (IConciergeData | null | undefined)[]
  ): IConciergeData[] {
    const conciergeData: IConciergeData[] = conciergeDataToCheck.filter(isPresent);
    if (conciergeData.length > 0) {
      const conciergeDataCollectionIdentifiers = conciergeDataCollection.map(
        conciergeDataItem => getConciergeDataIdentifier(conciergeDataItem)!
      );
      const conciergeDataToAdd = conciergeData.filter(conciergeDataItem => {
        const conciergeDataIdentifier = getConciergeDataIdentifier(conciergeDataItem);
        if (conciergeDataIdentifier == null || conciergeDataCollectionIdentifiers.includes(conciergeDataIdentifier)) {
          return false;
        }
        conciergeDataCollectionIdentifiers.push(conciergeDataIdentifier);
        return true;
      });
      return [...conciergeDataToAdd, ...conciergeDataCollection];
    }
    return conciergeDataCollection;
  }
}
