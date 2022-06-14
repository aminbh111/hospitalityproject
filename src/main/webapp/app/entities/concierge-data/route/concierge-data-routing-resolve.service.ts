import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConciergeData, ConciergeData } from '../concierge-data.model';
import { ConciergeDataService } from '../service/concierge-data.service';

@Injectable({ providedIn: 'root' })
export class ConciergeDataRoutingResolveService implements Resolve<IConciergeData> {
  constructor(protected service: ConciergeDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConciergeData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((conciergeData: HttpResponse<ConciergeData>) => {
          if (conciergeData.body) {
            return of(conciergeData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConciergeData());
  }
}
