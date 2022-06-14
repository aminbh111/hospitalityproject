import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContactUsComponent } from '../list/contact-us.component';
import { ContactUsDetailComponent } from '../detail/contact-us-detail.component';
import { ContactUsUpdateComponent } from '../update/contact-us-update.component';
import { ContactUsRoutingResolveService } from './contact-us-routing-resolve.service';

const contactUsRoute: Routes = [
  {
    path: '',
    component: ContactUsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContactUsDetailComponent,
    resolve: {
      contactUs: ContactUsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContactUsUpdateComponent,
    resolve: {
      contactUs: ContactUsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContactUsUpdateComponent,
    resolve: {
      contactUs: ContactUsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contactUsRoute)],
  exports: [RouterModule],
})
export class ContactUsRoutingModule {}
