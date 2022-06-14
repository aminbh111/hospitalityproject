import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBars } from '../bars.model';
import { BarsService } from '../service/bars.service';
import { BarsDeleteDialogComponent } from '../delete/bars-delete-dialog.component';

@Component({
  selector: 'jhi-bars',
  templateUrl: './bars.component.html',
})
export class BarsComponent implements OnInit {
  bars?: IBars[];
  isLoading = false;

  constructor(protected barsService: BarsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.barsService.query().subscribe({
      next: (res: HttpResponse<IBars[]>) => {
        this.isLoading = false;
        this.bars = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBars): number {
    return item.id!;
  }

  delete(bars: IBars): void {
    const modalRef = this.modalService.open(BarsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bars = bars;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
