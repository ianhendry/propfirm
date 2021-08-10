import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserCommentComponent } from '../list/user-comment.component';
import { UserCommentDetailComponent } from '../detail/user-comment-detail.component';
import { UserCommentUpdateComponent } from '../update/user-comment-update.component';
import { UserCommentRoutingResolveService } from './user-comment-routing-resolve.service';

const userCommentRoute: Routes = [
  {
    path: '',
    component: UserCommentComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserCommentDetailComponent,
    resolve: {
      userComment: UserCommentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserCommentUpdateComponent,
    resolve: {
      userComment: UserCommentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserCommentUpdateComponent,
    resolve: {
      userComment: UserCommentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userCommentRoute)],
  exports: [RouterModule],
})
export class UserCommentRoutingModule {}
