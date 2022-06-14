import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactUs } from '../contact-us.model';
import { ContactUsService } from '../service/contact-us.service';

@Component({
  templateUrl: './contact-us-delete-dialog.component.html',
})
export class ContactUsDeleteDialogComponent {
  contactUs?: IContactUs;

  constructor(protected contactUsService: ContactUsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contactUsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
