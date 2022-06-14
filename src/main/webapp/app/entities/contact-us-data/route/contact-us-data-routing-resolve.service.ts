import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContactUsData, ContactUsData } from '../contact-us-data.model';
import { ContactUsDataService } from '../service/contact-us-data.service';

@Injectable({ providedIn: 'root' })
export class ContactUsDataRoutingResolveService implements Resolve<IContactUsData> {
  constructor(protected service: ContactUsDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContactUsData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contactUsData: HttpResponse<ContactUsData>) => {
          if (contactUsData.body) {
            return of(contactUsData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContactUsData());
  }
}
