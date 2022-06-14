import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventData } from '../event-data.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-event-data-detail',
  templateUrl: './event-data-detail.component.html',
})
export class EventDataDetailComponent implements OnInit {
  eventData: IEventData | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventData }) => {
      this.eventData = eventData;
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
