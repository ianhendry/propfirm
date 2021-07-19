import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountDataTimeSeriesComponent } from './list/account-data-time-series.component';
import { AccountDataTimeSeriesDetailComponent } from './detail/account-data-time-series-detail.component';
import { AccountDataTimeSeriesUpdateComponent } from './update/account-data-time-series-update.component';
import { AccountDataTimeSeriesDeleteDialogComponent } from './delete/account-data-time-series-delete-dialog.component';
import { AccountDataTimeSeriesRoutingModule } from './route/account-data-time-series-routing.module';

@NgModule({
  imports: [SharedModule, AccountDataTimeSeriesRoutingModule],
  declarations: [
    AccountDataTimeSeriesComponent,
    AccountDataTimeSeriesDetailComponent,
    AccountDataTimeSeriesUpdateComponent,
    AccountDataTimeSeriesDeleteDialogComponent,
  ],
  entryComponents: [AccountDataTimeSeriesDeleteDialogComponent],
})
export class AccountDataTimeSeriesModule {}
