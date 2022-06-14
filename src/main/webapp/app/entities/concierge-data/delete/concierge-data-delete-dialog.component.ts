import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConciergeData } from '../concierge-data.model';
import { ConciergeDataService } from '../service/concierge-data.service';

@Component({
  templateUrl: './concierge-data-delete-dialog.component.html',
})
export class ConciergeDataDeleteDialogComponent {
  conciergeData?: IConciergeData;

  constructor(protected conciergeDataService: ConciergeDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.conciergeDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
