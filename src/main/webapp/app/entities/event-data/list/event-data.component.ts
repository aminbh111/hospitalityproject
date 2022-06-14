import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventData } from '../event-data.model';
import { EventDataService } from '../service/event-data.service';
import { EventDataDeleteDialogComponent } from '../delete/event-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-event-data',
  templateUrl: './event-data.component.html',
})
export class EventDataComponent implements OnInit {
  eventData?: IEventData[];
  isLoading = false;

  constructor(protected eventDataService: EventDataService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.eventDataService.query().subscribe({
      next: (res: HttpResponse<IEventData[]>) => {
        this.isLoading = false;
        this.eventData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IEventData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(eventData: IEventData): void {
    const modalRef = this.modalService.open(EventDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.eventData = eventData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
