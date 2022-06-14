import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomService } from '../room-service.model';

@Component({
  selector: 'jhi-room-service-detail',
  templateUrl: './room-service-detail.component.html',
})
export class RoomServiceDetailComponent implements OnInit {
  roomService: IRoomService | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomService }) => {
      this.roomService = roomService;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
