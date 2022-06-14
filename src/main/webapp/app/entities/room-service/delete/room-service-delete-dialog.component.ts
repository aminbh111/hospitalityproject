import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoomService } from '../room-service.model';
import { RoomServiceService } from '../service/room-service.service';

@Component({
  templateUrl: './room-service-delete-dialog.component.html',
})
export class RoomServiceDeleteDialogComponent {
  roomService?: IRoomService;

  constructor(protected roomServiceService: RoomServiceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roomServiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
