import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeetingData } from '../meeting-data.model';
import { MeetingDataService } from '../service/meeting-data.service';
import { MeetingDataDeleteDialogComponent } from '../delete/meeting-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-meeting-data',
  templateUrl: './meeting-data.component.html',
})
export class MeetingDataComponent implements OnInit {
  meetingData?: IMeetingData[];
  isLoading = false;

  constructor(protected meetingDataService: MeetingDataService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.meetingDataService.query().subscribe({
      next: (res: HttpResponse<IMeetingData[]>) => {
        this.isLoading = false;
        this.meetingData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMeetingData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(meetingData: IMeetingData): void {
    const modalRef = this.modalService.open(MeetingDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.meetingData = meetingData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
