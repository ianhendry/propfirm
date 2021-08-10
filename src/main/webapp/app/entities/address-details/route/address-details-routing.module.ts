import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AddressDetailsComponent } from '../list/address-details.component';
import { AddressDetailsDetailComponent } from '../detail/address-details-detail.component';
import { AddressDetailsUpdateComponent } from '../update/address-details-update.component';
import { AddressDetailsRoutingResolveService } from './address-details-routing-resolve.service';

const addressDetailsRoute: Routes = [
  {
    path: '',
    component: AddressDetailsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AddressDetailsDetailComponent,
    resolve: {
      addressDetails: AddressDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AddressDetailsUpdateComponent,
    resolve: {
      addressDetails: AddressDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AddressDetailsUpdateComponent,
    resolve: {
      addressDetails: AddressDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(addressDetailsRoute)],
  exports: [RouterModule],
})
export class AddressDetailsRoutingModule {}
