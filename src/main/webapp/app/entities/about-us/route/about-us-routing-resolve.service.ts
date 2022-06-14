import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAboutUs, AboutUs } from '../about-us.model';
import { AboutUsService } from '../service/about-us.service';

@Injectable({ providedIn: 'root' })
export class AboutUsRoutingResolveService implements Resolve<IAboutUs> {
  constructor(protected service: AboutUsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAboutUs> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aboutUs: HttpResponse<AboutUs>) => {
          if (aboutUs.body) {
            return of(aboutUs.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AboutUs());
  }
}
