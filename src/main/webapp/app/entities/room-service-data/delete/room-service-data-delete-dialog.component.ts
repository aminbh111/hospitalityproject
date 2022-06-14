import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoomServiceData } from '../room-service-data.model';
import { RoomServiceDataService } from '../service/room-service-data.service';

@Component({
  templateUrl: './room-service-data-delete-dialog.component.html',
})
export class RoomServiceDataDeleteDialogComponent {
  roomServiceData?: IRoomServiceData;

  constructor(protected roomServiceDataService: RoomServiceDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roomServiceDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
