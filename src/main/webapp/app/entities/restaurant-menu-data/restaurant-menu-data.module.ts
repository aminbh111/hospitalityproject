import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RestaurantMenuDataComponent } from './list/restaurant-menu-data.component';
import { RestaurantMenuDataDetailComponent } from './detail/restaurant-menu-data-detail.component';
import { RestaurantMenuDataUpdateComponent } from './update/restaurant-menu-data-update.component';
import { RestaurantMenuDataDeleteDialogComponent } from './delete/restaurant-menu-data-delete-dialog.component';
import { RestaurantMenuDataRoutingModule } from './route/restaurant-menu-data-routing.module';

@NgModule({
  imports: [SharedModule, RestaurantMenuDataRoutingModule],
  declarations: [
    RestaurantMenuDataComponent,
    RestaurantMenuDataDetailComponent,
    RestaurantMenuDataUpdateComponent,
    RestaurantMenuDataDeleteDialogComponent,
  ],
  entryComponents: [RestaurantMenuDataDeleteDialogComponent],
})
export class RestaurantMenuDataModule {}
