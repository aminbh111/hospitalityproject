import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MeetingDataComponent } from './list/meeting-data.component';
import { MeetingDataDetailComponent } from './detail/meeting-data-detail.component';
import { MeetingDataUpdateComponent } from './update/meeting-data-update.component';
import { MeetingDataDeleteDialogComponent } from './delete/meeting-data-delete-dialog.component';
import { MeetingDataRoutingModule } from './route/meeting-data-routing.module';

@NgModule({
  imports: [SharedModule, MeetingDataRoutingModule],
  declarations: [MeetingDataComponent, MeetingDataDetailComponent, MeetingDataUpdateComponent, MeetingDataDeleteDialogComponent],
  entryComponents: [MeetingDataDeleteDialogComponent],
})
export class MeetingDataModule {}
