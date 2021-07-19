import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISiteAccount, SiteAccount } from '../site-account.model';
import { SiteAccountService } from '../service/site-account.service';

@Injectable({ providedIn: 'root' })
export class SiteAccountRoutingResolveService implements Resolve<ISiteAccount> {
  constructor(protected service: SiteAccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISiteAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((siteAccount: HttpResponse<SiteAccount>) => {
          if (siteAccount.body) {
            return of(siteAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SiteAccount());
  }
}
