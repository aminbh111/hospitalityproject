import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MeetingComponent } from './list/meeting.component';
import { MeetingDetailComponent } from './detail/meeting-detail.component';
import { MeetingUpdateComponent } from './update/meeting-update.component';
import { MeetingDeleteDialogComponent } from './delete/meeting-delete-dialog.component';
import { MeetingRoutingModule } from './route/meeting-routing.module';

@NgModule({
  imports: [SharedModule, MeetingRoutingModule],
  declarations: [MeetingComponent, MeetingDetailComponent, MeetingUpdateComponent, MeetingDeleteDialogComponent],
  entryComponents: [MeetingDeleteDialogComponent],
})
export class MeetingModule {}
