import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoomServiceDataComponent } from '../list/room-service-data.component';
import { RoomServiceDataDetailComponent } from '../detail/room-service-data-detail.component';
import { RoomServiceDataUpdateComponent } from '../update/room-service-data-update.component';
import { RoomServiceDataRoutingResolveService } from './room-service-data-routing-resolve.service';

const roomServiceDataRoute: Routes = [
  {
    path: '',
    component: RoomServiceDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomServiceDataDetailComponent,
    resolve: {
      roomServiceData: RoomServiceDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoomServiceDataUpdateComponent,
    resolve: {
      roomServiceData: RoomServiceDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoomServiceDataUpdateComponent,
    resolve: {
      roomServiceData: RoomServiceDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roomServiceDataRoute)],
  exports: [RouterModule],
})
export class RoomServiceDataRoutingModule {}
