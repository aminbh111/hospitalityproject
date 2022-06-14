import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConcierge } from '../concierge.model';
import { ConciergeService } from '../service/concierge.service';

@Component({
  templateUrl: './concierge-delete-dialog.component.html',
})
export class ConciergeDeleteDialogComponent {
  concierge?: IConcierge;

  constructor(protected conciergeService: ConciergeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.conciergeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
