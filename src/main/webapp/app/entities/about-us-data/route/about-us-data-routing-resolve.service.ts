import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAboutUsData, AboutUsData } from '../about-us-data.model';
import { AboutUsDataService } from '../service/about-us-data.service';

@Injectable({ providedIn: 'root' })
export class AboutUsDataRoutingResolveService implements Resolve<IAboutUsData> {
  constructor(protected service: AboutUsDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAboutUsData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aboutUsData: HttpResponse<AboutUsData>) => {
          if (aboutUsData.body) {
            return of(aboutUsData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AboutUsData());
  }
}
