import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AboutUsComponent } from '../list/about-us.component';
import { AboutUsDetailComponent } from '../detail/about-us-detail.component';
import { AboutUsUpdateComponent } from '../update/about-us-update.component';
import { AboutUsRoutingResolveService } from './about-us-routing-resolve.service';

const aboutUsRoute: Routes = [
  {
    path: '',
    component: AboutUsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AboutUsDetailComponent,
    resolve: {
      aboutUs: AboutUsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AboutUsUpdateComponent,
    resolve: {
      aboutUs: AboutUsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AboutUsUpdateComponent,
    resolve: {
      aboutUs: AboutUsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aboutUsRoute)],
  exports: [RouterModule],
})
export class AboutUsRoutingModule {}
