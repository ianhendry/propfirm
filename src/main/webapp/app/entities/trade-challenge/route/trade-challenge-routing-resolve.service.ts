import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITradeChallenge, TradeChallenge } from '../trade-challenge.model';
import { TradeChallengeService } from '../service/trade-challenge.service';

@Injectable({ providedIn: 'root' })
export class TradeChallengeRoutingResolveService implements Resolve<ITradeChallenge> {
  constructor(protected service: TradeChallengeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITradeChallenge> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tradeChallenge: HttpResponse<TradeChallenge>) => {
          if (tradeChallenge.body) {
            return of(tradeChallenge.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TradeChallenge());
  }
}
