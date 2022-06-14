import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConciergeComponent } from './list/concierge.component';
import { ConciergeDetailComponent } from './detail/concierge-detail.component';
import { ConciergeUpdateComponent } from './update/concierge-update.component';
import { ConciergeDeleteDialogComponent } from './delete/concierge-delete-dialog.component';
import { ConciergeRoutingModule } from './route/concierge-routing.module';

@NgModule({
  imports: [SharedModule, ConciergeRoutingModule],
  declarations: [ConciergeComponent, ConciergeDetailComponent, ConciergeUpdateComponent, ConciergeDeleteDialogComponent],
  entryComponents: [ConciergeDeleteDialogComponent],
})
export class ConciergeModule {}
