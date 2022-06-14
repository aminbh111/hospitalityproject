import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlaylist } from '../playlist.model';
import { PlaylistService } from '../service/playlist.service';

@Component({
  templateUrl: './playlist-delete-dialog.component.html',
})
export class PlaylistDeleteDialogComponent {
  playlist?: IPlaylist;

  constructor(protected playlistService: PlaylistService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playlistService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
