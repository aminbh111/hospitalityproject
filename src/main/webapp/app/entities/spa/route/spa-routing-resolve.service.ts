import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpa, Spa } from '../spa.model';
import { SpaService } from '../service/spa.service';

@Injectable({ providedIn: 'root' })
export class SpaRoutingResolveService implements Resolve<ISpa> {
  constructor(protected service: SpaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((spa: HttpResponse<Spa>) => {
          if (spa.body) {
            return of(spa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Spa());
  }
}
