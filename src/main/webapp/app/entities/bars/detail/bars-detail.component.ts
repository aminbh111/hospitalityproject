import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBars } from '../bars.model';

@Component({
  selector: 'jhi-bars-detail',
  templateUrl: './bars-detail.component.html',
})
export class BarsDetailComponent implements OnInit {
  bars: IBars | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bars }) => {
      this.bars = bars;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
