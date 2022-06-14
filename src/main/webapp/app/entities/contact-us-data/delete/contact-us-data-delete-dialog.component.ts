import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactUsData } from '../contact-us-data.model';
import { ContactUsDataService } from '../service/contact-us-data.service';

@Component({
  templateUrl: './contact-us-data-delete-dialog.component.html',
})
export class ContactUsDataDeleteDialogComponent {
  contactUsData?: IContactUsData;

  constructor(protected contactUsDataService: ContactUsDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactUsDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
