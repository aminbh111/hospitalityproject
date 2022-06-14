import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAboutUs } from '../about-us.model';
import { AboutUsService } from '../service/about-us.service';
import { AboutUsDeleteDialogComponent } from '../delete/about-us-delete-dialog.component';

@Component({
  selector: 'jhi-about-us',
  templateUrl: './about-us.component.html',
})
export class AboutUsComponent implements OnInit {
  aboutuses?: IAboutUs[];
  isLoading = false;

  constructor(protected aboutUsService: AboutUsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.aboutUsService.query().subscribe({
      next: (res: HttpResponse<IAboutUs[]>) => {
        this.isLoading = false;
        this.aboutuses = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAboutUs): number {
    return item.id!;
  }

  delete(aboutUs: IAboutUs): void {
    const modalRef = this.modalService.open(AboutUsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aboutUs = aboutUs;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
