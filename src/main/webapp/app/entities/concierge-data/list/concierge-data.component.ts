import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IConciergeData } from '../concierge-data.model';
import { ConciergeDataService } from '../service/concierge-data.service';
import { ConciergeDataDeleteDialogComponent } from '../delete/concierge-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-concierge-data',
  templateUrl: './concierge-data.component.html',
})
export class ConciergeDataComponent implements OnInit {
  conciergeData?: IConciergeData[];
  isLoading = false;

  constructor(protected conciergeDataService: ConciergeDataService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.conciergeDataService.query().subscribe({
      next: (res: HttpResponse<IConciergeData[]>) => {
        this.isLoading = false;
        this.conciergeData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IConciergeData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(conciergeData: IConciergeData): void {
    const modalRef = this.modalService.open(ConciergeDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.conciergeData = conciergeData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
