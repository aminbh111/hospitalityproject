import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRestaurantMenuData, getRestaurantMenuDataIdentifier } from '../restaurant-menu-data.model';

export type EntityResponseType = HttpResponse<IRestaurantMenuData>;
export type EntityArrayResponseType = HttpResponse<IRestaurantMenuData[]>;

@Injectable({ providedIn: 'root' })
export class RestaurantMenuDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restaurant-menu-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restaurantMenuData: IRestaurantMenuData): Observable<EntityResponseType> {
    return this.http.post<IRestaurantMenuData>(this.resourceUrl, restaurantMenuData, { observe: 'response' });
  }

  update(restaurantMenuData: IRestaurantMenuData): Observable<EntityResponseType> {
    return this.http.put<IRestaurantMenuData>(
      `${this.resourceUrl}/${getRestaurantMenuDataIdentifier(restaurantMenuData) as number}`,
      restaurantMenuData,
      { observe: 'response' }
    );
  }

  partialUpdate(restaurantMenuData: IRestaurantMenuData): Observable<EntityResponseType> {
    return this.http.patch<IRestaurantMenuData>(
      `${this.resourceUrl}/${getRestaurantMenuDataIdentifier(restaurantMenuData) as number}`,
      restaurantMenuData,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRestaurantMenuData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestaurantMenuData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRestaurantMenuDataToCollectionIfMissing(
    restaurantMenuDataCollection: IRestaurantMenuData[],
    ...restaurantMenuDataToCheck: (IRestaurantMenuData | null | undefined)[]
  ): IRestaurantMenuData[] {
    const restaurantMenuData: IRestaurantMenuData[] = restaurantMenuDataToCheck.filter(isPresent);
    if (restaurantMenuData.length > 0) {
      const restaurantMenuDataCollectionIdentifiers = restaurantMenuDataCollection.map(
        restaurantMenuDataItem => getRestaurantMenuDataIdentifier(restaurantMenuDataItem)!
      );
      const restaurantMenuDataToAdd = restaurantMenuData.filter(restaurantMenuDataItem => {
        const restaurantMenuDataIdentifier = getRestaurantMenuDataIdentifier(restaurantMenuDataItem);
        if (restaurantMenuDataIdentifier == null || restaurantMenuDataCollectionIdentifiers.includes(restaurantMenuDataIdentifier)) {
          return false;
        }
        restaurantMenuDataCollectionIdentifiers.push(restaurantMenuDataIdentifier);
        return true;
      });
      return [...restaurantMenuDataToAdd, ...restaurantMenuDataCollection];
    }
    return restaurantMenuDataCollection;
  }
}
