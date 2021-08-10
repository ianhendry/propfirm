import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMt4Trade } from '../mt-4-trade.model';
import { Mt4TradeService } from '../service/mt-4-trade.service';

@Component({
  templateUrl: './mt-4-trade-delete-dialog.component.html',
})
export class Mt4TradeDeleteDialogComponent {
  mt4Trade?: IMt4Trade;

  constructor(protected mt4TradeService: Mt4TradeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mt4TradeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
