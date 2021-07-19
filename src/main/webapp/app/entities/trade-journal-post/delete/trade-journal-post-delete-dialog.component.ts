import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITradeJournalPost } from '../trade-journal-post.model';
import { TradeJournalPostService } from '../service/trade-journal-post.service';

@Component({
  templateUrl: './trade-journal-post-delete-dialog.component.html',
})
export class TradeJournalPostDeleteDialogComponent {
  tradeJournalPost?: ITradeJournalPost;

  constructor(protected tradeJournalPostService: TradeJournalPostService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tradeJournalPostService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
