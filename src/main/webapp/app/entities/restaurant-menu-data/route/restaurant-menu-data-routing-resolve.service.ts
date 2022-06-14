import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRestaurantMenuData, RestaurantMenuData } from '../restaurant-menu-data.model';
import { RestaurantMenuDataService } from '../service/restaurant-menu-data.service';

@Injectable({ providedIn: 'root' })
export class RestaurantMenuDataRoutingResolveService implements Resolve<IRestaurantMenuData> {
  constructor(protected service: RestaurantMenuDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRestaurantMenuData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((restaurantMenuData: HttpResponse<RestaurantMenuData>) => {
          if (restaurantMenuData.body) {
            return of(restaurantMenuData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RestaurantMenuData());
  }
}
