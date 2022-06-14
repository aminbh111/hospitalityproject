import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RoomServiceDataComponent } from './list/room-service-data.component';
import { RoomServiceDataDetailComponent } from './detail/room-service-data-detail.component';
import { RoomServiceDataUpdateComponent } from './update/room-service-data-update.component';
import { RoomServiceDataDeleteDialogComponent } from './delete/room-service-data-delete-dialog.component';
import { RoomServiceDataRoutingModule } from './route/room-service-data-routing.module';

@NgModule({
  imports: [SharedModule, RoomServiceDataRoutingModule],
  declarations: [
    RoomServiceDataComponent,
    RoomServiceDataDetailComponent,
    RoomServiceDataUpdateComponent,
    RoomServiceDataDeleteDialogComponent,
  ],
  entryComponents: [RoomServiceDataDeleteDialogComponent],
})
export class RoomServiceDataModule {}
