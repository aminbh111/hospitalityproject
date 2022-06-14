import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AboutUsComponent } from './list/about-us.component';
import { AboutUsDetailComponent } from './detail/about-us-detail.component';
import { AboutUsUpdateComponent } from './update/about-us-update.component';
import { AboutUsDeleteDialogComponent } from './delete/about-us-delete-dialog.component';
import { AboutUsRoutingModule } from './route/about-us-routing.module';

@NgModule({
  imports: [SharedModule, AboutUsRoutingModule],
  declarations: [AboutUsComponent, AboutUsDetailComponent, AboutUsUpdateComponent, AboutUsDeleteDialogComponent],
  entryComponents: [AboutUsDeleteDialogComponent],
})
export class AboutUsModule {}
