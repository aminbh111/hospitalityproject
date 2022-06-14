import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAboutUs } from '../about-us.model';

@Component({
  selector: 'jhi-about-us-detail',
  templateUrl: './about-us-detail.component.html',
})
export class AboutUsDetailComponent implements OnInit {
  aboutUs: IAboutUs | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aboutUs }) => {
      this.aboutUs = aboutUs;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
