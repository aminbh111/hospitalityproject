import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OfferDataComponent } from '../list/offer-data.component';
import { OfferDataDetailComponent } from '../detail/offer-data-detail.component';
import { OfferDataUpdateComponent } from '../update/offer-data-update.component';
import { OfferDataRoutingResolveService } from './offer-data-routing-resolve.service';

const offerDataRoute: Routes = [
  {
    path: '',
    component: OfferDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OfferDataDetailComponent,
    resolve: {
      offerData: OfferDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OfferDataUpdateComponent,
    resolve: {
      offerData: OfferDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OfferDataUpdateComponent,
    resolve: {
      offerData: OfferDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(offerDataRoute)],
  exports: [RouterModule],
})
export class OfferDataRoutingModule {}
