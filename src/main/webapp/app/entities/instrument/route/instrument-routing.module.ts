import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InstrumentComponent } from '../list/instrument.component';
import { InstrumentDetailComponent } from '../detail/instrument-detail.component';
import { InstrumentUpdateComponent } from '../update/instrument-update.component';
import { InstrumentRoutingResolveService } from './instrument-routing-resolve.service';

const instrumentRoute: Routes = [
  {
    path: '',
    component: InstrumentComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InstrumentDetailComponent,
    resolve: {
      instrument: InstrumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InstrumentUpdateComponent,
    resolve: {
      instrument: InstrumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InstrumentUpdateComponent,
    resolve: {
      instrument: InstrumentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(instrumentRoute)],
  exports: [RouterModule],
})
export class InstrumentRoutingModule {}
