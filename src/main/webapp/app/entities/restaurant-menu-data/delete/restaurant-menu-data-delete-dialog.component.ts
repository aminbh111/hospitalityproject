import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaurantMenuData } from '../restaurant-menu-data.model';
import { RestaurantMenuDataService } from '../service/restaurant-menu-data.service';

@Component({
  templateUrl: './restaurant-menu-data-delete-dialog.component.html',
})
export class RestaurantMenuDataDeleteDialogComponent {
  restaurantMenuData?: IRestaurantMenuData;

  constructor(protected restaurantMenuDataService: RestaurantMenuDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.restaurantMenuDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
