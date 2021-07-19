import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Mt4AccountComponent } from '../list/mt-4-account.component';
import { Mt4AccountDetailComponent } from '../detail/mt-4-account-detail.component';
import { Mt4AccountUpdateComponent } from '../update/mt-4-account-update.component';
import { Mt4AccountRoutingResolveService } from './mt-4-account-routing-resolve.service';

const mt4AccountRoute: Routes = [
  {
    path: '',
    component: Mt4AccountComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: Mt4AccountDetailComponent,
    resolve: {
      mt4Account: Mt4AccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: Mt4AccountUpdateComponent,
    resolve: {
      mt4Account: Mt4AccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: Mt4AccountUpdateComponent,
    resolve: {
      mt4Account: Mt4AccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mt4AccountRoute)],
  exports: [RouterModule],
})
export class Mt4AccountRoutingModule {}
