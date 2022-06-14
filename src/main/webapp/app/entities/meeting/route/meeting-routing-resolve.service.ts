import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMeeting, Meeting } from '../meeting.model';
import { MeetingService } from '../service/meeting.service';

@Injectable({ providedIn: 'root' })
export class MeetingRoutingResolveService implements Resolve<IMeeting> {
  constructor(protected service: MeetingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMeeting> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((meeting: HttpResponse<Meeting>) => {
          if (meeting.body) {
            return of(meeting.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Meeting());
  }
}
