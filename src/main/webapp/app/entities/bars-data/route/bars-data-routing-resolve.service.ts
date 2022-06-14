import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBarsData, BarsData } from '../bars-data.model';
import { BarsDataService } from '../service/bars-data.service';

@Injectable({ providedIn: 'root' })
export class BarsDataRoutingResolveService implements Resolve<IBarsData> {
  constructor(protected service: BarsDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBarsData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((barsData: HttpResponse<BarsData>) => {
          if (barsData.body) {
            return of(barsData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BarsData());
  }
}
