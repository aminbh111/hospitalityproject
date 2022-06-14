import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BarsDataComponent } from '../list/bars-data.component';
import { BarsDataDetailComponent } from '../detail/bars-data-detail.component';
import { BarsDataUpdateComponent } from '../update/bars-data-update.component';
import { BarsDataRoutingResolveService } from './bars-data-routing-resolve.service';

const barsDataRoute: Routes = [
  {
    path: '',
    component: BarsDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BarsDataDetailComponent,
    resolve: {
      barsData: BarsDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BarsDataUpdateComponent,
    resolve: {
      barsData: BarsDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BarsDataUpdateComponent,
    resolve: {
      barsData: BarsDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(barsDataRoute)],
  exports: [RouterModule],
})
export class BarsDataRoutingModule {}
