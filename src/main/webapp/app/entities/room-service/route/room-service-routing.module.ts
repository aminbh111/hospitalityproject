import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoomServiceComponent } from '../list/room-service.component';
import { RoomServiceDetailComponent } from '../detail/room-service-detail.component';
import { RoomServiceUpdateComponent } from '../update/room-service-update.component';
import { RoomServiceRoutingResolveService } from './room-service-routing-resolve.service';

const roomServiceRoute: Routes = [
  {
    path: '',
    component: RoomServiceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomServiceDetailComponent,
    resolve: {
      roomService: RoomServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoomServiceUpdateComponent,
    resolve: {
      roomService: RoomServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoomServiceUpdateComponent,
    resolve: {
      roomService: RoomServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roomServiceRoute)],
  exports: [RouterModule],
})
export class RoomServiceRoutingModule {}
