import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOfferData, OfferData } from '../offer-data.model';
import { OfferDataService } from '../service/offer-data.service';

@Injectable({ providedIn: 'root' })
export class OfferDataRoutingResolveService implements Resolve<IOfferData> {
  constructor(protected service: OfferDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOfferData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((offerData: HttpResponse<OfferData>) => {
          if (offerData.body) {
            return of(offerData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OfferData());
  }
}
