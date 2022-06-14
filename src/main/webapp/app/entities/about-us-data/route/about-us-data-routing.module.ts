import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AboutUsDataComponent } from '../list/about-us-data.component';
import { AboutUsDataDetailComponent } from '../detail/about-us-data-detail.component';
import { AboutUsDataUpdateComponent } from '../update/about-us-data-update.component';
import { AboutUsDataRoutingResolveService } from './about-us-data-routing-resolve.service';

const aboutUsDataRoute: Routes = [
  {
    path: '',
    component: AboutUsDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AboutUsDataDetailComponent,
    resolve: {
      aboutUsData: AboutUsDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AboutUsDataUpdateComponent,
    resolve: {
      aboutUsData: AboutUsDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AboutUsDataUpdateComponent,
    resolve: {
      aboutUsData: AboutUsDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aboutUsDataRoute)],
  exports: [RouterModule],
})
export class AboutUsDataRoutingModule {}
