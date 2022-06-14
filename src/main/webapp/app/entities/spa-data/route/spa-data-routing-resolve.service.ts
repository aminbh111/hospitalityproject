import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpaData, SpaData } from '../spa-data.model';
import { SpaDataService } from '../service/spa-data.service';

@Injectable({ providedIn: 'root' })
export class SpaDataRoutingResolveService implements Resolve<ISpaData> {
  constructor(protected service: SpaDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpaData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((spaData: HttpResponse<SpaData>) => {
          if (spaData.body) {
            return of(spaData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SpaData());
  }
}
