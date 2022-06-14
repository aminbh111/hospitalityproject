import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConciergeComponent } from '../list/concierge.component';
import { ConciergeDetailComponent } from '../detail/concierge-detail.component';
import { ConciergeUpdateComponent } from '../update/concierge-update.component';
import { ConciergeRoutingResolveService } from './concierge-routing-resolve.service';

const conciergeRoute: Routes = [
  {
    path: '',
    component: ConciergeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConciergeDetailComponent,
    resolve: {
      concierge: ConciergeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConciergeUpdateComponent,
    resolve: {
      concierge: ConciergeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConciergeUpdateComponent,
    resolve: {
      concierge: ConciergeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(conciergeRoute)],
  exports: [RouterModule],
})
export class ConciergeRoutingModule {}
