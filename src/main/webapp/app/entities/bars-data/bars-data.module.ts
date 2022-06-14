import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BarsDataComponent } from './list/bars-data.component';
import { BarsDataDetailComponent } from './detail/bars-data-detail.component';
import { BarsDataUpdateComponent } from './update/bars-data-update.component';
import { BarsDataDeleteDialogComponent } from './delete/bars-data-delete-dialog.component';
import { BarsDataRoutingModule } from './route/bars-data-routing.module';

@NgModule({
  imports: [SharedModule, BarsDataRoutingModule],
  declarations: [BarsDataComponent, BarsDataDetailComponent, BarsDataUpdateComponent, BarsDataDeleteDialogComponent],
  entryComponents: [BarsDataDeleteDialogComponent],
})
export class BarsDataModule {}
