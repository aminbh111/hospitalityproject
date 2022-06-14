import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MeetingDataComponent } from '../list/meeting-data.component';
import { MeetingDataDetailComponent } from '../detail/meeting-data-detail.component';
import { MeetingDataUpdateComponent } from '../update/meeting-data-update.component';
import { MeetingDataRoutingResolveService } from './meeting-data-routing-resolve.service';

const meetingDataRoute: Routes = [
  {
    path: '',
    component: MeetingDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeetingDataDetailComponent,
    resolve: {
      meetingData: MeetingDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeetingDataUpdateComponent,
    resolve: {
      meetingData: MeetingDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeetingDataUpdateComponent,
    resolve: {
      meetingData: MeetingDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(meetingDataRoute)],
  exports: [RouterModule],
})
export class MeetingDataRoutingModule {}
