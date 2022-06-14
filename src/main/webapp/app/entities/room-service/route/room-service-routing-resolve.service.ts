import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoomService, RoomService } from '../room-service.model';
import { RoomServiceService } from '../service/room-service.service';

@Injectable({ providedIn: 'root' })
export class RoomServiceRoutingResolveService implements Resolve<IRoomService> {
  constructor(protected service: RoomServiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoomService> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roomService: HttpResponse<RoomService>) => {
          if (roomService.body) {
            return of(roomService.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RoomService());
  }
}
