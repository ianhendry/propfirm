import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDailyAnalysisPost, DailyAnalysisPost } from '../daily-analysis-post.model';
import { DailyAnalysisPostService } from '../service/daily-analysis-post.service';

@Injectable({ providedIn: 'root' })
export class DailyAnalysisPostRoutingResolveService implements Resolve<IDailyAnalysisPost> {
  constructor(protected service: DailyAnalysisPostService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDailyAnalysisPost> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dailyAnalysisPost: HttpResponse<DailyAnalysisPost>) => {
          if (dailyAnalysisPost.body) {
            return of(dailyAnalysisPost.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DailyAnalysisPost());
  }
}
