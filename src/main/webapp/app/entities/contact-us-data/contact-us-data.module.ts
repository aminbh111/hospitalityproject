import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactUsDataComponent } from './list/contact-us-data.component';
import { ContactUsDataDetailComponent } from './detail/contact-us-data-detail.component';
import { ContactUsDataUpdateComponent } from './update/contact-us-data-update.component';
import { ContactUsDataDeleteDialogComponent } from './delete/contact-us-data-delete-dialog.component';
import { ContactUsDataRoutingModule } from './route/contact-us-data-routing.module';

@NgModule({
  imports: [SharedModule, ContactUsDataRoutingModule],
  declarations: [ContactUsDataComponent, ContactUsDataDetailComponent, ContactUsDataUpdateComponent, ContactUsDataDeleteDialogComponent],
  entryComponents: [ContactUsDataDeleteDialogComponent],
})
export class ContactUsDataModule {}
