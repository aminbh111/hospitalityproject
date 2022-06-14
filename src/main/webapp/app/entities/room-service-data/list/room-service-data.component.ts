import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoomServiceData } from '../room-service-data.model';
import { RoomServiceDataService } from '../service/room-service-data.service';
import { RoomServiceDataDeleteDialogComponent } from '../delete/room-service-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-room-service-data',
  templateUrl: './room-service-data.component.html',
})
export class RoomServiceDataComponent implements OnInit {
  roomServiceData?: IRoomServiceData[];
  isLoading = false;

  constructor(protected roomServiceDataService: RoomServiceDataService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.roomServiceDataService.query().subscribe({
      next: (res: HttpResponse<IRoomServiceData[]>) => {
        this.isLoading = false;
        this.roomServiceData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRoomServiceData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(roomServiceData: IRoomServiceData): void {
    const modalRef = this.modalService.open(RoomServiceDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.roomServiceData = roomServiceData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
