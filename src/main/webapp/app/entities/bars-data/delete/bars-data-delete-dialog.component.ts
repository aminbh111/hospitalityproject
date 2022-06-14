import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBarsData } from '../bars-data.model';
import { BarsDataService } from '../service/bars-data.service';

@Component({
  templateUrl: './bars-data-delete-dialog.component.html',
})
export class BarsDataDeleteDialogComponent {
  barsData?: IBarsData;

  constructor(protected barsDataService: BarsDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.barsDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
