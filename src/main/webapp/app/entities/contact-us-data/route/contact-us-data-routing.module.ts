import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactUsDataComponent } from '../list/contact-us-data.component';
import { ContactUsDataDetailComponent } from '../detail/contact-us-data-detail.component';
import { ContactUsDataUpdateComponent } from '../update/contact-us-data-update.component';
import { ContactUsDataRoutingResolveService } from './contact-us-data-routing-resolve.service';

const contactUsDataRoute: Routes = [
  {
    path: '',
    component: ContactUsDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactUsDataDetailComponent,
    resolve: {
      contactUsData: ContactUsDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactUsDataUpdateComponent,
    resolve: {
      contactUsData: ContactUsDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactUsDataUpdateComponent,
    resolve: {
      contactUsData: ContactUsDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactUsDataRoute)],
  exports: [RouterModule],
})
export class ContactUsDataRoutingModule {}
