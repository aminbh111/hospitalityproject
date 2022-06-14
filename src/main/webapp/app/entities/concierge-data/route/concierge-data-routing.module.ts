import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConciergeDataComponent } from '../list/concierge-data.component';
import { ConciergeDataDetailComponent } from '../detail/concierge-data-detail.component';
import { ConciergeDataUpdateComponent } from '../update/concierge-data-update.component';
import { ConciergeDataRoutingResolveService } from './concierge-data-routing-resolve.service';

const conciergeDataRoute: Routes = [
  {
    path: '',
    component: ConciergeDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConciergeDataDetailComponent,
    resolve: {
      conciergeData: ConciergeDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConciergeDataUpdateComponent,
    resolve: {
      conciergeData: ConciergeDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConciergeDataUpdateComponent,
    resolve: {
      conciergeData: ConciergeDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(conciergeDataRoute)],
  exports: [RouterModule],
})
export class ConciergeDataRoutingModule {}
