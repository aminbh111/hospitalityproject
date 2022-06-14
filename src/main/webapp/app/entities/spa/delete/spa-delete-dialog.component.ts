import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpa } from '../spa.model';
import { SpaService } from '../service/spa.service';

@Component({
  templateUrl: './spa-delete-dialog.component.html',
})
export class SpaDeleteDialogComponent {
  spa?: ISpa;

  constructor(protected spaService: SpaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.spaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
