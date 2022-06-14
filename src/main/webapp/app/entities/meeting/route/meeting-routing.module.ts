import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MeetingComponent } from '../list/meeting.component';
import { MeetingDetailComponent } from '../detail/meeting-detail.component';
import { MeetingUpdateComponent } from '../update/meeting-update.component';
import { MeetingRoutingResolveService } from './meeting-routing-resolve.service';

const meetingRoute: Routes = [
  {
    path: '',
    component: MeetingComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeetingDetailComponent,
    resolve: {
      meeting: MeetingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeetingUpdateComponent,
    resolve: {
      meeting: MeetingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeetingUpdateComponent,
    resolve: {
      meeting: MeetingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(meetingRoute)],
  exports: [RouterModule],
})
export class MeetingRoutingModule {}
