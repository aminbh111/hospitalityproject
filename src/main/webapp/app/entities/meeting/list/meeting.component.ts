import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeeting } from '../meeting.model';
import { MeetingService } from '../service/meeting.service';
import { MeetingDeleteDialogComponent } from '../delete/meeting-delete-dialog.component';

@Component({
  selector: 'jhi-meeting',
  templateUrl: './meeting.component.html',
})
export class MeetingComponent implements OnInit {
  meetings?: IMeeting[];
  isLoading = false;

  constructor(protected meetingService: MeetingService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.meetingService.query().subscribe({
      next: (res: HttpResponse<IMeeting[]>) => {
        this.isLoading = false;
        this.meetings = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMeeting): number {
    return item.id!;
  }

  delete(meeting: IMeeting): void {
    const modalRef = this.modalService.open(MeetingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.meeting = meeting;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
