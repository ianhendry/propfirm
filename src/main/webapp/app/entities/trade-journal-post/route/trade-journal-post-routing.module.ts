import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TradeJournalPostComponent } from '../list/trade-journal-post.component';
import { TradeJournalPostDetailComponent } from '../detail/trade-journal-post-detail.component';
import { TradeJournalPostUpdateComponent } from '../update/trade-journal-post-update.component';
import { TradeJournalPostRoutingResolveService } from './trade-journal-post-routing-resolve.service';

const tradeJournalPostRoute: Routes = [
  {
    path: '',
    component: TradeJournalPostComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TradeJournalPostDetailComponent,
    resolve: {
      tradeJournalPost: TradeJournalPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TradeJournalPostUpdateComponent,
    resolve: {
      tradeJournalPost: TradeJournalPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TradeJournalPostUpdateComponent,
    resolve: {
      tradeJournalPost: TradeJournalPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tradeJournalPostRoute)],
  exports: [RouterModule],
})
export class TradeJournalPostRoutingModule {}
