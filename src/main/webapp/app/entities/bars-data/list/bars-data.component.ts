import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBarsData } from '../bars-data.model';
import { BarsDataService } from '../service/bars-data.service';
import { BarsDataDeleteDialogComponent } from '../delete/bars-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-bars-data',
  templateUrl: './bars-data.component.html',
})
export class BarsDataComponent implements OnInit {
  barsData?: IBarsData[];
  isLoading = false;

  constructor(protected barsDataService: BarsDataService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.barsDataService.query().subscribe({
      next: (res: HttpResponse<IBarsData[]>) => {
        this.isLoading = false;
        this.barsData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBarsData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(barsData: IBarsData): void {
    const modalRef = this.modalService.open(BarsDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.barsData = barsData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
