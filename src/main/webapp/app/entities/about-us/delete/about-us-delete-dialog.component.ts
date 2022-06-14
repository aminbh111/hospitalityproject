import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAboutUs } from '../about-us.model';
import { AboutUsService } from '../service/about-us.service';

@Component({
  templateUrl: './about-us-delete-dialog.component.html',
})
export class AboutUsDeleteDialogComponent {
  aboutUs?: IAboutUs;

  constructor(protected aboutUsService: AboutUsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aboutUsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
