import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BarsComponent } from '../list/bars.component';
import { BarsDetailComponent } from '../detail/bars-detail.component';
import { BarsUpdateComponent } from '../update/bars-update.component';
import { BarsRoutingResolveService } from './bars-routing-resolve.service';

const barsRoute: Routes = [
  {
    path: '',
    component: BarsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BarsDetailComponent,
    resolve: {
      bars: BarsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BarsUpdateComponent,
    resolve: {
      bars: BarsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BarsUpdateComponent,
    resolve: {
      bars: BarsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(barsRoute)],
  exports: [RouterModule],
})
export class BarsRoutingModule {}
