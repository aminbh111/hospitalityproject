import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConcierge } from '../concierge.model';

@Component({
  selector: 'jhi-concierge-detail',
  templateUrl: './concierge-detail.component.html',
})
export class ConciergeDetailComponent implements OnInit {
  concierge: IConcierge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concierge }) => {
      this.concierge = concierge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
