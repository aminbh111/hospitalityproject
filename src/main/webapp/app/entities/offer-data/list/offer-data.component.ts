import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOfferData } from '../offer-data.model';
import { OfferDataService } from '../service/offer-data.service';
import { OfferDataDeleteDialogComponent } from '../delete/offer-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-offer-data',
  templateUrl: './offer-data.component.html',
})
export class OfferDataComponent implements OnInit {
  offerData?: IOfferData[];
  isLoading = false;

  constructor(protected offerDataService: OfferDataService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.offerDataService.query().subscribe({
      next: (res: HttpResponse<IOfferData[]>) => {
        this.isLoading = false;
        this.offerData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IOfferData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(offerData: IOfferData): void {
    const modalRef = this.modalService.open(OfferDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.offerData = offerData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
