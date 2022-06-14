import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ServiceDataComponent } from './list/service-data.component';
import { ServiceDataDetailComponent } from './detail/service-data-detail.component';
import { ServiceDataUpdateComponent } from './update/service-data-update.component';
import { ServiceDataDeleteDialogComponent } from './delete/service-data-delete-dialog.component';
import { ServiceDataRoutingModule } from './route/service-data-routing.module';

@NgModule({
  imports: [SharedModule, ServiceDataRoutingModule],
  declarations: [ServiceDataComponent, ServiceDataDetailComponent, ServiceDataUpdateComponent, ServiceDataDeleteDialogComponent],
  entryComponents: [ServiceDataDeleteDialogComponent],
})
export class ServiceDataModule {}
