import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChallengeType, ChallengeType } from '../challenge-type.model';
import { ChallengeTypeService } from '../service/challenge-type.service';

@Injectable({ providedIn: 'root' })
export class ChallengeTypeRoutingResolveService implements Resolve<IChallengeType> {
  constructor(protected service: ChallengeTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChallengeType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((challengeType: HttpResponse<ChallengeType>) => {
          if (challengeType.body) {
            return of(challengeType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChallengeType());
  }
}
