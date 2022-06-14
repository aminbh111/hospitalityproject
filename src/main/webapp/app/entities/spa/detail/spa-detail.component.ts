import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpa } from '../spa.model';

@Component({
  selector: 'jhi-spa-detail',
  templateUrl: './spa-detail.component.html',
})
export class SpaDetailComponent implements OnInit {
  spa: ISpa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spa }) => {
      this.spa = spa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
