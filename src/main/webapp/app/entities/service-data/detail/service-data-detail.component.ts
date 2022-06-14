import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServiceData } from '../service-data.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-service-data-detail',
  templateUrl: './service-data-detail.component.html',
})
export class ServiceDataDetailComponent implements OnInit {
  serviceData: IServiceData | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceData }) => {
      this.serviceData = serviceData;
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
