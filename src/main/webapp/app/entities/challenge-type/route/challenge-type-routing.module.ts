import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChallengeTypeComponent } from '../list/challenge-type.component';
import { ChallengeTypeDetailComponent } from '../detail/challenge-type-detail.component';
import { ChallengeTypeUpdateComponent } from '../update/challenge-type-update.component';
import { ChallengeTypeRoutingResolveService } from './challenge-type-routing-resolve.service';

const challengeTypeRoute: Routes = [
  {
    path: '',
    component: ChallengeTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChallengeTypeDetailComponent,
    resolve: {
      challengeType: ChallengeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChallengeTypeUpdateComponent,
    resolve: {
      challengeType: ChallengeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChallengeTypeUpdateComponent,
    resolve: {
      challengeType: ChallengeTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(challengeTypeRoute)],
  exports: [RouterModule],
})
export class ChallengeTypeRoutingModule {}
