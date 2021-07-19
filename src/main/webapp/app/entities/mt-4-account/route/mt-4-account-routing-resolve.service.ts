import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMt4Account, Mt4Account } from '../mt-4-account.model';
import { Mt4AccountService } from '../service/mt-4-account.service';

@Injectable({ providedIn: 'root' })
export class Mt4AccountRoutingResolveService implements Resolve<IMt4Account> {
  constructor(protected service: Mt4AccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMt4Account> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mt4Account: HttpResponse<Mt4Account>) => {
          if (mt4Account.body) {
            return of(mt4Account.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mt4Account());
  }
}
