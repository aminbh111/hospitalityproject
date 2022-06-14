import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SpaDataComponent } from './list/spa-data.component';
import { SpaDataDetailComponent } from './detail/spa-data-detail.component';
import { SpaDataUpdateComponent } from './update/spa-data-update.component';
import { SpaDataDeleteDialogComponent } from './delete/spa-data-delete-dialog.component';
import { SpaDataRoutingModule } from './route/spa-data-routing.module';

@NgModule({
  imports: [SharedModule, SpaDataRoutingModule],
  declarations: [SpaDataComponent, SpaDataDetailComponent, SpaDataUpdateComponent, SpaDataDeleteDialogComponent],
  entryComponents: [SpaDataDeleteDialogComponent],
})
export class SpaDataModule {}
