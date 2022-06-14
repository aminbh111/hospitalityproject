import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpaData } from '../spa-data.model';
import { SpaDataService } from '../service/spa-data.service';
import { SpaDataDeleteDialogComponent } from '../delete/spa-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-spa-data',
  templateUrl: './spa-data.component.html',
})
export class SpaDataComponent implements OnInit {
  spaData?: ISpaData[];
  isLoading = false;

  constructor(protected spaDataService: SpaDataService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.spaDataService.query().subscribe({
      next: (res: HttpResponse<ISpaData[]>) => {
        this.isLoading = false;
        this.spaData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISpaData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(spaData: ISpaData): void {
    const modalRef = this.modalService.open(SpaDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.spaData = spaData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
