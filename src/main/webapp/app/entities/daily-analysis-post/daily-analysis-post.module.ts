import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DailyAnalysisPostComponent } from './list/daily-analysis-post.component';
import { DailyAnalysisPostDetailComponent } from './detail/daily-analysis-post-detail.component';
import { DailyAnalysisPostUpdateComponent } from './update/daily-analysis-post-update.component';
import { DailyAnalysisPostDeleteDialogComponent } from './delete/daily-analysis-post-delete-dialog.component';
import { DailyAnalysisPostRoutingModule } from './route/daily-analysis-post-routing.module';

@NgModule({
  imports: [SharedModule, DailyAnalysisPostRoutingModule],
  declarations: [
    DailyAnalysisPostComponent,
    DailyAnalysisPostDetailComponent,
    DailyAnalysisPostUpdateComponent,
    DailyAnalysisPostDeleteDialogComponent,
  ],
  entryComponents: [DailyAnalysisPostDeleteDialogComponent],
})
export class DailyAnalysisPostModule {}
