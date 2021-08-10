import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITradeChallenge } from '../trade-challenge.model';
import { TradeChallengeService } from '../service/trade-challenge.service';

@Component({
  templateUrl: './trade-challenge-delete-dialog.component.html',
})
export class TradeChallengeDeleteDialogComponent {
  tradeChallenge?: ITradeChallenge;

  constructor(protected tradeChallengeService: TradeChallengeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tradeChallengeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
