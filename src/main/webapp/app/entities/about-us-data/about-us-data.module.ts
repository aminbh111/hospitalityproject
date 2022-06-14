import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AboutUsDataComponent } from './list/about-us-data.component';
import { AboutUsDataDetailComponent } from './detail/about-us-data-detail.component';
import { AboutUsDataUpdateComponent } from './update/about-us-data-update.component';
import { AboutUsDataDeleteDialogComponent } from './delete/about-us-data-delete-dialog.component';
import { AboutUsDataRoutingModule } from './route/about-us-data-routing.module';

@NgModule({
  imports: [SharedModule, AboutUsDataRoutingModule],
  declarations: [AboutUsDataComponent, AboutUsDataDetailComponent, AboutUsDataUpdateComponent, AboutUsDataDeleteDialogComponent],
  entryComponents: [AboutUsDataDeleteDialogComponent],
})
export class AboutUsDataModule {}
