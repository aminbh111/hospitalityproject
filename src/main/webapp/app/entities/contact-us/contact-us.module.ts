import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContactUsComponent } from './list/contact-us.component';
import { ContactUsDetailComponent } from './detail/contact-us-detail.component';
import { ContactUsUpdateComponent } from './update/contact-us-update.component';
import { ContactUsDeleteDialogComponent } from './delete/contact-us-delete-dialog.component';
import { ContactUsRoutingModule } from './route/contact-us-routing.module';

@NgModule({
  imports: [SharedModule, ContactUsRoutingModule],
  declarations: [ContactUsComponent, ContactUsDetailComponent, ContactUsUpdateComponent, ContactUsDeleteDialogComponent],
  entryComponents: [ContactUsDeleteDialogComponent],
})
export class ContactUsModule {}
