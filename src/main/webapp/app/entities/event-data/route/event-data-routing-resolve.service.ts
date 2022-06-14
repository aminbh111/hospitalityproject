import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEventData, EventData } from '../event-data.model';
import { EventDataService } from '../service/event-data.service';

@Injectable({ providedIn: 'root' })
export class EventDataRoutingResolveService implements Resolve<IEventData> {
  constructor(protected service: EventDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eventData: HttpResponse<EventData>) => {
          if (eventData.body) {
            return of(eventData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EventData());
  }
}
