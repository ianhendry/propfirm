import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountDataTimeSeriesComponent } from '../list/account-data-time-series.component';
import { AccountDataTimeSeriesDetailComponent } from '../detail/account-data-time-series-detail.component';
import { AccountDataTimeSeriesUpdateComponent } from '../update/account-data-time-series-update.component';
import { AccountDataTimeSeriesRoutingResolveService } from './account-data-time-series-routing-resolve.service';

const accountDataTimeSeriesRoute: Routes = [
  {
    path: '',
    component: AccountDataTimeSeriesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountDataTimeSeriesDetailComponent,
    resolve: {
      accountDataTimeSeries: AccountDataTimeSeriesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountDataTimeSeriesUpdateComponent,
    resolve: {
      accountDataTimeSeries: AccountDataTimeSeriesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountDataTimeSeriesUpdateComponent,
    resolve: {
      accountDataTimeSeries: AccountDataTimeSeriesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountDataTimeSeriesRoute)],
  exports: [RouterModule],
})
export class AccountDataTimeSeriesRoutingModule {}
