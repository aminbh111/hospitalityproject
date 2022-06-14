import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BarsComponent } from './list/bars.component';
import { BarsDetailComponent } from './detail/bars-detail.component';
import { BarsUpdateComponent } from './update/bars-update.component';
import { BarsDeleteDialogComponent } from './delete/bars-delete-dialog.component';
import { BarsRoutingModule } from './route/bars-routing.module';

@NgModule({
  imports: [SharedModule, BarsRoutingModule],
  declarations: [BarsComponent, BarsDetailComponent, BarsUpdateComponent, BarsDeleteDialogComponent],
  entryComponents: [BarsDeleteDialogComponent],
})
export class BarsModule {}
