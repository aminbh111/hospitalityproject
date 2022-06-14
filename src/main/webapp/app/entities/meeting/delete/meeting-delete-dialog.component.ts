import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeeting } from '../meeting.model';
import { MeetingService } from '../service/meeting.service';

@Component({
  templateUrl: './meeting-delete-dialog.component.html',
})
export class MeetingDeleteDialogComponent {
  meeting?: IMeeting;

  constructor(protected meetingService: MeetingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.meetingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
