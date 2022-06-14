import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SpaComponent } from '../list/spa.component';
import { SpaDetailComponent } from '../detail/spa-detail.component';
import { SpaUpdateComponent } from '../update/spa-update.component';
import { SpaRoutingResolveService } from './spa-routing-resolve.service';

const spaRoute: Routes = [
  {
    path: '',
    component: SpaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpaDetailComponent,
    resolve: {
      spa: SpaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpaUpdateComponent,
    resolve: {
      spa: SpaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpaUpdateComponent,
    resolve: {
      spa: SpaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(spaRoute)],
  exports: [RouterModule],
})
export class SpaRoutingModule {}
