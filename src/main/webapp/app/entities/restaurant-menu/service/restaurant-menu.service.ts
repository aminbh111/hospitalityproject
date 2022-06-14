import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRestaurantMenu, getRestaurantMenuIdentifier } from '../restaurant-menu.model';

export type EntityResponseType = HttpResponse<IRestaurantMenu>;
export type EntityArrayResponseType = HttpResponse<IRestaurantMenu[]>;

@Injectable({ providedIn: 'root' })
export class RestaurantMenuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restaurant-menus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restaurantMenu: IRestaurantMenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restaurantMenu);
    return this.http
      .post<IRestaurantMenu>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(restaurantMenu: IRestaurantMenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restaurantMenu);
    return this.http
      .put<IRestaurantMenu>(`${this.resourceUrl}/${getRestaurantMenuIdentifier(restaurantMenu) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(restaurantMenu: IRestaurantMenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restaurantMenu);
    return this.http
      .patch<IRestaurantMenu>(`${this.resourceUrl}/${getRestaurantMenuIdentifier(restaurantMenu) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRestaurantMenu>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRestaurantMenu[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRestaurantMenuToCollectionIfMissing(
    restaurantMenuCollection: IRestaurantMenu[],
    ...restaurantMenusToCheck: (IRestaurantMenu | null | undefined)[]
  ): IRestaurantMenu[] {
    const restaurantMenus: IRestaurantMenu[] = restaurantMenusToCheck.filter(isPresent);
    if (restaurantMenus.length > 0) {
      const restaurantMenuCollectionIdentifiers = restaurantMenuCollection.map(
        restaurantMenuItem => getRestaurantMenuIdentifier(restaurantMenuItem)!
      );
      const restaurantMenusToAdd = restaurantMenus.filter(restaurantMenuItem => {
        const restaurantMenuIdentifier = getRestaurantMenuIdentifier(restaurantMenuItem);
        if (restaurantMenuIdentifier == null || restaurantMenuCollectionIdentifiers.includes(restaurantMenuIdentifier)) {
          return false;
        }
        restaurantMenuCollectionIdentifiers.push(restaurantMenuIdentifier);
        return true;
      });
      return [...restaurantMenusToAdd, ...restaurantMenuCollection];
    }
    return restaurantMenuCollection;
  }

  protected convertDateFromClient(restaurantMenu: IRestaurantMenu): IRestaurantMenu {
    return Object.assign({}, restaurantMenu, {
      date: restaurantMenu.date?.isValid() ? restaurantMenu.date.toJSON() : undefined,
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
      res.body.forEach((restaurantMenu: IRestaurantMenu) => {
        restaurantMenu.date = restaurantMenu.date ? dayjs(restaurantMenu.date) : undefined;
      });
    }
    return res;
  }
}
