import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OfferDataComponent } from './list/offer-data.component';
import { OfferDataDetailComponent } from './detail/offer-data-detail.component';
import { OfferDataUpdateComponent } from './update/offer-data-update.component';
import { OfferDataDeleteDialogComponent } from './delete/offer-data-delete-dialog.component';
import { OfferDataRoutingModule } from './route/offer-data-routing.module';

@NgModule({
  imports: [SharedModule, OfferDataRoutingModule],
  declarations: [OfferDataComponent, OfferDataDetailComponent, OfferDataUpdateComponent, OfferDataDeleteDialogComponent],
  entryComponents: [OfferDataDeleteDialogComponent],
})
export class OfferDataModule {}
