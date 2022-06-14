import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpa } from '../spa.model';
import { SpaService } from '../service/spa.service';
import { SpaDeleteDialogComponent } from '../delete/spa-delete-dialog.component';

@Component({
  selector: 'jhi-spa',
  templateUrl: './spa.component.html',
})
export class SpaComponent implements OnInit {
  spas?: ISpa[];
  isLoading = false;

  constructor(protected spaService: SpaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.spaService.query().subscribe({
      next: (res: HttpResponse<ISpa[]>) => {
        this.isLoading = false;
        this.spas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISpa): number {
    return item.id!;
  }

  delete(spa: ISpa): void {
    const modalRef = this.modalService.open(SpaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.spa = spa;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
