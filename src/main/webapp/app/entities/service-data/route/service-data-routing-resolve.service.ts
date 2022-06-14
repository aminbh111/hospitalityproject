import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceData, ServiceData } from '../service-data.model';
import { ServiceDataService } from '../service/service-data.service';

@Injectable({ providedIn: 'root' })
export class ServiceDataRoutingResolveService implements Resolve<IServiceData> {
  constructor(protected service: ServiceDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServiceData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((serviceData: HttpResponse<ServiceData>) => {
          if (serviceData.body) {
            return of(serviceData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServiceData());
  }
}
