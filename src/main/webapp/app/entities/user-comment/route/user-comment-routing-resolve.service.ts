import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserComment, UserComment } from '../user-comment.model';
import { UserCommentService } from '../service/user-comment.service';

@Injectable({ providedIn: 'root' })
export class UserCommentRoutingResolveService implements Resolve<IUserComment> {
  constructor(protected service: UserCommentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserComment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userComment: HttpResponse<UserComment>) => {
          if (userComment.body) {
            return of(userComment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserComment());
  }
}
