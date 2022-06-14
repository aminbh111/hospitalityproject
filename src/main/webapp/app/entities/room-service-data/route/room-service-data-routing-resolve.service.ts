import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoomServiceData, RoomServiceData } from '../room-service-data.model';
import { RoomServiceDataService } from '../service/room-service-data.service';

@Injectable({ providedIn: 'root' })
export class RoomServiceDataRoutingResolveService implements Resolve<IRoomServiceData> {
  constructor(protected service: RoomServiceDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoomServiceData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roomServiceData: HttpResponse<RoomServiceData>) => {
          if (roomServiceData.body) {
            return of(roomServiceData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RoomServiceData());
  }
}
