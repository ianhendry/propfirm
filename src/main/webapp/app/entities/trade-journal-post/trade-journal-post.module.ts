import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TradeJournalPostComponent } from './list/trade-journal-post.component';
import { TradeJournalPostDetailComponent } from './detail/trade-journal-post-detail.component';
import { TradeJournalPostUpdateComponent } from './update/trade-journal-post-update.component';
import { TradeJournalPostDeleteDialogComponent } from './delete/trade-journal-post-delete-dialog.component';
import { TradeJournalPostRoutingModule } from './route/trade-journal-post-routing.module';

@NgModule({
  imports: [SharedModule, TradeJournalPostRoutingModule],
  declarations: [
    TradeJournalPostComponent,
    TradeJournalPostDetailComponent,
    TradeJournalPostUpdateComponent,
    TradeJournalPostDeleteDialogComponent,
  ],
  entryComponents: [TradeJournalPostDeleteDialogComponent],
})
export class TradeJournalPostModule {}
