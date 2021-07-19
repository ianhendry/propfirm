import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TradeChallengeComponent } from './list/trade-challenge.component';
import { TradeChallengeDetailComponent } from './detail/trade-challenge-detail.component';
import { TradeChallengeUpdateComponent } from './update/trade-challenge-update.component';
import { TradeChallengeDeleteDialogComponent } from './delete/trade-challenge-delete-dialog.component';
import { TradeChallengeRoutingModule } from './route/trade-challenge-routing.module';

@NgModule({
  imports: [SharedModule, TradeChallengeRoutingModule],
  declarations: [
    TradeChallengeComponent,
    TradeChallengeDetailComponent,
    TradeChallengeUpdateComponent,
    TradeChallengeDeleteDialogComponent,
  ],
  entryComponents: [TradeChallengeDeleteDialogComponent],
})
export class TradeChallengeModule {}
