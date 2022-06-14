import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAboutUsData } from '../about-us-data.model';
import { AboutUsDataService } from '../service/about-us-data.service';

@Component({
  templateUrl: './about-us-data-delete-dialog.component.html',
})
export class AboutUsDataDeleteDialogComponent {
  aboutUsData?: IAboutUsData;

  constructor(protected aboutUsDataService: AboutUsDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aboutUsDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
