import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountDataTimeSeries, AccountDataTimeSeries } from '../account-data-time-series.model';
import { AccountDataTimeSeriesService } from '../service/account-data-time-series.service';

@Injectable({ providedIn: 'root' })
export class AccountDataTimeSeriesRoutingResolveService implements Resolve<IAccountDataTimeSeries> {
  constructor(protected service: AccountDataTimeSeriesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccountDataTimeSeries> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accountDataTimeSeries: HttpResponse<AccountDataTimeSeries>) => {
          if (accountDataTimeSeries.body) {
            return of(accountDataTimeSeries.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AccountDataTimeSeries());
  }
}
