import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConcierge, Concierge } from '../concierge.model';
import { ConciergeService } from '../service/concierge.service';

@Injectable({ providedIn: 'root' })
export class ConciergeRoutingResolveService implements Resolve<IConcierge> {
  constructor(protected service: ConciergeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConcierge> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((concierge: HttpResponse<Concierge>) => {
          if (concierge.body) {
            return of(concierge.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Concierge());
  }
}
