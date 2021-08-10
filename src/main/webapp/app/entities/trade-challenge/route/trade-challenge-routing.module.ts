import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TradeChallengeComponent } from '../list/trade-challenge.component';
import { TradeChallengeDetailComponent } from '../detail/trade-challenge-detail.component';
import { TradeChallengeUpdateComponent } from '../update/trade-challenge-update.component';
import { TradeChallengeRoutingResolveService } from './trade-challenge-routing-resolve.service';

const tradeChallengeRoute: Routes = [
  {
    path: '',
    component: TradeChallengeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TradeChallengeDetailComponent,
    resolve: {
      tradeChallenge: TradeChallengeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TradeChallengeUpdateComponent,
    resolve: {
      tradeChallenge: TradeChallengeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TradeChallengeUpdateComponent,
    resolve: {
      tradeChallenge: TradeChallengeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tradeChallengeRoute)],
  exports: [RouterModule],
})
export class TradeChallengeRoutingModule {}
