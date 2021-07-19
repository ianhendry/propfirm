import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SiteAccountComponent } from '../list/site-account.component';
import { SiteAccountDetailComponent } from '../detail/site-account-detail.component';
import { SiteAccountUpdateComponent } from '../update/site-account-update.component';
import { SiteAccountRoutingResolveService } from './site-account-routing-resolve.service';

const siteAccountRoute: Routes = [
  {
    path: '',
    component: SiteAccountComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SiteAccountDetailComponent,
    resolve: {
      siteAccount: SiteAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SiteAccountUpdateComponent,
    resolve: {
      siteAccount: SiteAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SiteAccountUpdateComponent,
    resolve: {
      siteAccount: SiteAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(siteAccountRoute)],
  exports: [RouterModule],
})
export class SiteAccountRoutingModule {}
