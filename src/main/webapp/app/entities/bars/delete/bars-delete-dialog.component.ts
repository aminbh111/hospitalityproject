import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBars } from '../bars.model';
import { BarsService } from '../service/bars.service';

@Component({
  templateUrl: './bars-delete-dialog.component.html',
})
export class BarsDeleteDialogComponent {
  bars?: IBars;

  constructor(protected barsService: BarsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.barsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
