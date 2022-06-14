import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOfferData } from '../offer-data.model';
import { OfferDataService } from '../service/offer-data.service';

@Component({
  templateUrl: './offer-data-delete-dialog.component.html',
})
export class OfferDataDeleteDialogComponent {
  offerData?: IOfferData;

  constructor(protected offerDataService: OfferDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.offerDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
