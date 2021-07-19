import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DailyAnalysisPostComponent } from '../list/daily-analysis-post.component';
import { DailyAnalysisPostDetailComponent } from '../detail/daily-analysis-post-detail.component';
import { DailyAnalysisPostUpdateComponent } from '../update/daily-analysis-post-update.component';
import { DailyAnalysisPostRoutingResolveService } from './daily-analysis-post-routing-resolve.service';

const dailyAnalysisPostRoute: Routes = [
  {
    path: '',
    component: DailyAnalysisPostComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DailyAnalysisPostDetailComponent,
    resolve: {
      dailyAnalysisPost: DailyAnalysisPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DailyAnalysisPostUpdateComponent,
    resolve: {
      dailyAnalysisPost: DailyAnalysisPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DailyAnalysisPostUpdateComponent,
    resolve: {
      dailyAnalysisPost: DailyAnalysisPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dailyAnalysisPostRoute)],
  exports: [RouterModule],
})
export class DailyAnalysisPostRoutingModule {}
