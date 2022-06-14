import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoomService } from '../room-service.model';
import { RoomServiceService } from '../service/room-service.service';
import { RoomServiceDeleteDialogComponent } from '../delete/room-service-delete-dialog.component';

@Component({
  selector: 'jhi-room-service',
  templateUrl: './room-service.component.html',
})
export class RoomServiceComponent implements OnInit {
  roomServices?: IRoomService[];
  isLoading = false;

  constructor(protected roomServiceService: RoomServiceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.roomServiceService.query().subscribe({
      next: (res: HttpResponse<IRoomService[]>) => {
        this.isLoading = false;
        this.roomServices = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRoomService): number {
    return item.id!;
  }

  delete(roomService: IRoomService): void {
    const modalRef = this.modalService.open(RoomServiceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.roomService = roomService;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
