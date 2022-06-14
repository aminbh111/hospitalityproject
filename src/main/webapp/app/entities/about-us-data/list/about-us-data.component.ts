import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAboutUsData } from '../about-us-data.model';
import { AboutUsDataService } from '../service/about-us-data.service';
import { AboutUsDataDeleteDialogComponent } from '../delete/about-us-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-about-us-data',
  templateUrl: './about-us-data.component.html',
})
export class AboutUsDataComponent implements OnInit {
  aboutUsData?: IAboutUsData[];
  isLoading = false;

  constructor(protected aboutUsDataService: AboutUsDataService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.aboutUsDataService.query().subscribe({
      next: (res: HttpResponse<IAboutUsData[]>) => {
        this.isLoading = false;
        this.aboutUsData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAboutUsData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(aboutUsData: IAboutUsData): void {
    const modalRef = this.modalService.open(AboutUsDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aboutUsData = aboutUsData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
