import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EventDataComponent } from './list/event-data.component';
import { EventDataDetailComponent } from './detail/event-data-detail.component';
import { EventDataUpdateComponent } from './update/event-data-update.component';
import { EventDataDeleteDialogComponent } from './delete/event-data-delete-dialog.component';
import { EventDataRoutingModule } from './route/event-data-routing.module';

@NgModule({
  imports: [SharedModule, EventDataRoutingModule],
  declarations: [EventDataComponent, EventDataDetailComponent, EventDataUpdateComponent, EventDataDeleteDialogComponent],
  entryComponents: [EventDataDeleteDialogComponent],
})
export class EventDataModule {}
