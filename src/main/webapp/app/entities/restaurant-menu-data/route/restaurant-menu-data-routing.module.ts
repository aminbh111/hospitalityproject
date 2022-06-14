import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RestaurantMenuDataComponent } from '../list/restaurant-menu-data.component';
import { RestaurantMenuDataDetailComponent } from '../detail/restaurant-menu-data-detail.component';
import { RestaurantMenuDataUpdateComponent } from '../update/restaurant-menu-data-update.component';
import { RestaurantMenuDataRoutingResolveService } from './restaurant-menu-data-routing-resolve.service';

const restaurantMenuDataRoute: Routes = [
  {
    path: '',
    component: RestaurantMenuDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RestaurantMenuDataDetailComponent,
    resolve: {
      restaurantMenuData: RestaurantMenuDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RestaurantMenuDataUpdateComponent,
    resolve: {
      restaurantMenuData: RestaurantMenuDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RestaurantMenuDataUpdateComponent,
    resolve: {
      restaurantMenuData: RestaurantMenuDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(restaurantMenuDataRoute)],
  exports: [RouterModule],
})
export class RestaurantMenuDataRoutingModule {}
