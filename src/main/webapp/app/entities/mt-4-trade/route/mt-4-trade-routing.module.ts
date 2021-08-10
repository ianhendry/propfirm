import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Mt4TradeComponent } from '../list/mt-4-trade.component';
import { Mt4TradeDetailComponent } from '../detail/mt-4-trade-detail.component';
import { Mt4TradeUpdateComponent } from '../update/mt-4-trade-update.component';
import { Mt4TradeRoutingResolveService } from './mt-4-trade-routing-resolve.service';

const mt4TradeRoute: Routes = [
  {
    path: '',
    component: Mt4TradeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: Mt4TradeDetailComponent,
    resolve: {
      mt4Trade: Mt4TradeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: Mt4TradeUpdateComponent,
    resolve: {
      mt4Trade: Mt4TradeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: Mt4TradeUpdateComponent,
    resolve: {
      mt4Trade: Mt4TradeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mt4TradeRoute)],
  exports: [RouterModule],
})
export class Mt4TradeRoutingModule {}
