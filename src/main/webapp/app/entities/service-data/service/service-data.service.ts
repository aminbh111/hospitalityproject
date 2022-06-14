import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceData, getServiceDataIdentifier } from '../service-data.model';

export type EntityResponseType = HttpResponse<IServiceData>;
export type EntityArrayResponseType = HttpResponse<IServiceData[]>;

@Injectable({ providedIn: 'root' })
export class ServiceDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(serviceData: IServiceData): Observable<EntityResponseType> {
    return this.http.post<IServiceData>(this.resourceUrl, serviceData, { observe: 'response' });
  }

  update(serviceData: IServiceData): Observable<EntityResponseType> {
    return this.http.put<IServiceData>(`${this.resourceUrl}/${getServiceDataIdentifier(serviceData) as number}`, serviceData, {
      observe: 'response',
    });
  }

  partialUpdate(serviceData: IServiceData): Observable<EntityResponseType> {
    return this.http.patch<IServiceData>(`${this.resourceUrl}/${getServiceDataIdentifier(serviceData) as number}`, serviceData, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IServiceData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServiceData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addServiceDataToCollectionIfMissing(
    serviceDataCollection: IServiceData[],
    ...serviceDataToCheck: (IServiceData | null | undefined)[]
  ): IServiceData[] {
    const serviceData: IServiceData[] = serviceDataToCheck.filter(isPresent);
    if (serviceData.length > 0) {
      const serviceDataCollectionIdentifiers = serviceDataCollection.map(serviceDataItem => getServiceDataIdentifier(serviceDataItem)!);
      const serviceDataToAdd = serviceData.filter(serviceDataItem => {
        const serviceDataIdentifier = getServiceDataIdentifier(serviceDataItem);
        if (serviceDataIdentifier == null || serviceDataCollectionIdentifiers.includes(serviceDataIdentifier)) {
          return false;
        }
        serviceDataCollectionIdentifiers.push(serviceDataIdentifier);
        return true;
      });
      return [...serviceDataToAdd, ...serviceDataCollection];
    }
    return serviceDataCollection;
  }
}
