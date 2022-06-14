import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EventDataComponent } from '../list/event-data.component';
import { EventDataDetailComponent } from '../detail/event-data-detail.component';
import { EventDataUpdateComponent } from '../update/event-data-update.component';
import { EventDataRoutingResolveService } from './event-data-routing-resolve.service';

const eventDataRoute: Routes = [
  {
    path: '',
    component: EventDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventDataDetailComponent,
    resolve: {
      eventData: EventDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventDataUpdateComponent,
    resolve: {
      eventData: EventDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventDataUpdateComponent,
    resolve: {
      eventData: EventDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventDataRoute)],
  exports: [RouterModule],
})
export class EventDataRoutingModule {}
