import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeetingData } from '../meeting-data.model';
import { MeetingDataService } from '../service/meeting-data.service';

@Component({
  templateUrl: './meeting-data-delete-dialog.component.html',
})
export class MeetingDataDeleteDialogComponent {
  meetingData?: IMeetingData;

  constructor(protected meetingDataService: MeetingDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.meetingDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
