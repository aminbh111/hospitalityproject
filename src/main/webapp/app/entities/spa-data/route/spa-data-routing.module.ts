import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SpaDataComponent } from '../list/spa-data.component';
import { SpaDataDetailComponent } from '../detail/spa-data-detail.component';
import { SpaDataUpdateComponent } from '../update/spa-data-update.component';
import { SpaDataRoutingResolveService } from './spa-data-routing-resolve.service';

const spaDataRoute: Routes = [
  {
    path: '',
    component: SpaDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpaDataDetailComponent,
    resolve: {
      spaData: SpaDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpaDataUpdateComponent,
    resolve: {
      spaData: SpaDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpaDataUpdateComponent,
    resolve: {
      spaData: SpaDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(spaDataRoute)],
  exports: [RouterModule],
})
export class SpaDataRoutingModule {}
