import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpaData } from '../spa-data.model';
import { SpaDataService } from '../service/spa-data.service';

@Component({
  templateUrl: './spa-data-delete-dialog.component.html',
})
export class SpaDataDeleteDialogComponent {
  spaData?: ISpaData;

  constructor(protected spaDataService: SpaDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.spaDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
