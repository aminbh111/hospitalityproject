import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOfferData } from '../offer-data.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-offer-data-detail',
  templateUrl: './offer-data-detail.component.html',
})
export class OfferDataDetailComponent implements OnInit {
  offerData: IOfferData | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offerData }) => {
      this.offerData = offerData;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
