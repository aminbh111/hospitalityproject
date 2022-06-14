import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactUsData } from '../contact-us-data.model';
import { ContactUsDataService } from '../service/contact-us-data.service';
import { ContactUsDataDeleteDialogComponent } from '../delete/contact-us-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-contact-us-data',
  templateUrl: './contact-us-data.component.html',
})
export class ContactUsDataComponent implements OnInit {
  contactUsData?: IContactUsData[];
  isLoading = false;

  constructor(protected contactUsDataService: ContactUsDataService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.contactUsDataService.query().subscribe({
      next: (res: HttpResponse<IContactUsData[]>) => {
        this.isLoading = false;
        this.contactUsData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IContactUsData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(contactUsData: IContactUsData): void {
    const modalRef = this.modalService.open(ContactUsDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactUsData = contactUsData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
