import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactUs } from '../contact-us.model';

@Component({
  selector: 'jhi-contact-us-detail',
  templateUrl: './contact-us-detail.component.html',
})
export class ContactUsDetailComponent implements OnInit {
  contactUs: IContactUs | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactUs }) => {
      this.contactUs = contactUs;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
