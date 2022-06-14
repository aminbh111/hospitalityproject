import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConciergeDataComponent } from './list/concierge-data.component';
import { ConciergeDataDetailComponent } from './detail/concierge-data-detail.component';
import { ConciergeDataUpdateComponent } from './update/concierge-data-update.component';
import { ConciergeDataDeleteDialogComponent } from './delete/concierge-data-delete-dialog.component';
import { ConciergeDataRoutingModule } from './route/concierge-data-routing.module';

@NgModule({
  imports: [SharedModule, ConciergeDataRoutingModule],
  declarations: [ConciergeDataComponent, ConciergeDataDetailComponent, ConciergeDataUpdateComponent, ConciergeDataDeleteDialogComponent],
  entryComponents: [ConciergeDataDeleteDialogComponent],
})
export class ConciergeDataModule {}
