import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactUs, ContactUs } from '../contact-us.model';
import { ContactUsService } from '../service/contact-us.service';

@Injectable({ providedIn: 'root' })
export class ContactUsRoutingResolveService implements Resolve<IContactUs> {
  constructor(protected service: ContactUsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactUs> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactUs: HttpResponse<ContactUs>) => {
          if (contactUs.body) {
            return of(contactUs.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactUs());
  }
}
