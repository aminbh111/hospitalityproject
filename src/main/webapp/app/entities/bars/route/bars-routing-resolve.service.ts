import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBars, Bars } from '../bars.model';
import { BarsService } from '../service/bars.service';

@Injectable({ providedIn: 'root' })
export class BarsRoutingResolveService implements Resolve<IBars> {
  constructor(protected service: BarsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBars> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bars: HttpResponse<Bars>) => {
          if (bars.body) {
            return of(bars.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bars());
  }
}
