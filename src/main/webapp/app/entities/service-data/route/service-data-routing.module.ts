import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ServiceDataComponent } from '../list/service-data.component';
import { ServiceDataDetailComponent } from '../detail/service-data-detail.component';
import { ServiceDataUpdateComponent } from '../update/service-data-update.component';
import { ServiceDataRoutingResolveService } from './service-data-routing-resolve.service';

const serviceDataRoute: Routes = [
  {
    path: '',
    component: ServiceDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceDataDetailComponent,
    resolve: {
      serviceData: ServiceDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceDataUpdateComponent,
    resolve: {
      serviceData: ServiceDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceDataUpdateComponent,
    resolve: {
      serviceData: ServiceDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serviceDataRoute)],
  exports: [RouterModule],
})
export class ServiceDataRoutingModule {}
