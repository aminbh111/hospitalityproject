import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventData } from '../event-data.model';
import { EventDataService } from '../service/event-data.service';

@Component({
  templateUrl: './event-data-delete-dialog.component.html',
})
export class EventDataDeleteDialogComponent {
  eventData?: IEventData;

  constructor(protected eventDataService: EventDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
