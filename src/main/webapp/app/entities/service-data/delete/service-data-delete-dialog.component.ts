import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IServiceData } from '../service-data.model';
import { ServiceDataService } from '../service/service-data.service';

@Component({
  templateUrl: './service-data-delete-dialog.component.html',
})
export class ServiceDataDeleteDialogComponent {
  serviceData?: IServiceData;

  constructor(protected serviceDataService: ServiceDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
