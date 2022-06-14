import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RoomServiceComponent } from './list/room-service.component';
import { RoomServiceDetailComponent } from './detail/room-service-detail.component';
import { RoomServiceUpdateComponent } from './update/room-service-update.component';
import { RoomServiceDeleteDialogComponent } from './delete/room-service-delete-dialog.component';
import { RoomServiceRoutingModule } from './route/room-service-routing.module';

@NgModule({
  imports: [SharedModule, RoomServiceRoutingModule],
  declarations: [RoomServiceComponent, RoomServiceDetailComponent, RoomServiceUpdateComponent, RoomServiceDeleteDialogComponent],
  entryComponents: [RoomServiceDeleteDialogComponent],
})
export class RoomServiceModule {}
