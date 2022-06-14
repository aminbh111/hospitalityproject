import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactUs } from '../contact-us.model';
import { ContactUsService } from '../service/contact-us.service';
import { ContactUsDeleteDialogComponent } from '../delete/contact-us-delete-dialog.component';

@Component({
  selector: 'jhi-contact-us',
  templateUrl: './contact-us.component.html',
})
export class ContactUsComponent implements OnInit {
  contactuses?: IContactUs[];
  isLoading = false;

  constructor(protected contactUsService: ContactUsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.contactUsService.query().subscribe({
      next: (res: HttpResponse<IContactUs[]>) => {
        this.isLoading = false;
        this.contactuses = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IContactUs): number {
    return item.id!;
  }

  delete(contactUs: IContactUs): void {
    const modalRef = this.modalService.open(ContactUsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactUs = contactUs;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
