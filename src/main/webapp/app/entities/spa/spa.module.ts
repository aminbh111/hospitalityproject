import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SpaComponent } from './list/spa.component';
import { SpaDetailComponent } from './detail/spa-detail.component';
import { SpaUpdateComponent } from './update/spa-update.component';
import { SpaDeleteDialogComponent } from './delete/spa-delete-dialog.component';
import { SpaRoutingModule } from './route/spa-routing.module';

@NgModule({
  imports: [SharedModule, SpaRoutingModule],
  declarations: [SpaComponent, SpaDetailComponent, SpaUpdateComponent, SpaDeleteDialogComponent],
  entryComponents: [SpaDeleteDialogComponent],
})
export class SpaModule {}
