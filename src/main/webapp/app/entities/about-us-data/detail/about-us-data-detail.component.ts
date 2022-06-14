import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAboutUsData } from '../about-us-data.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-about-us-data-detail',
  templateUrl: './about-us-data-detail.component.html',
})
export class AboutUsDataDetailComponent implements OnInit {
  aboutUsData: IAboutUsData | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aboutUsData }) => {
      this.aboutUsData = aboutUsData;
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
