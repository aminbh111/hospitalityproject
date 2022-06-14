import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMeetingData, MeetingData } from '../meeting-data.model';
import { MeetingDataService } from '../service/meeting-data.service';

@Injectable({ providedIn: 'root' })
export class MeetingDataRoutingResolveService implements Resolve<IMeetingData> {
  constructor(protected service: MeetingDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMeetingData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((meetingData: HttpResponse<MeetingData>) => {
          if (meetingData.body) {
            return of(meetingData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MeetingData());
  }
}
