import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMt4Trade, Mt4Trade } from '../mt-4-trade.model';
import { Mt4TradeService } from '../service/mt-4-trade.service';

@Injectable({ providedIn: 'root' })
export class Mt4TradeRoutingResolveService implements Resolve<IMt4Trade> {
  constructor(protected service: Mt4TradeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMt4Trade> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mt4Trade: HttpResponse<Mt4Trade>) => {
          if (mt4Trade.body) {
            return of(mt4Trade.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mt4Trade());
  }
}
