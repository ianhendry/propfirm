import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITradeJournalPost, TradeJournalPost } from '../trade-journal-post.model';
import { TradeJournalPostService } from '../service/trade-journal-post.service';

@Injectable({ providedIn: 'root' })
export class TradeJournalPostRoutingResolveService implements Resolve<ITradeJournalPost> {
  constructor(protected service: TradeJournalPostService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITradeJournalPost> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tradeJournalPost: HttpResponse<TradeJournalPost>) => {
          if (tradeJournalPost.body) {
            return of(tradeJournalPost.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TradeJournalPost());
  }
}
