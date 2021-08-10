import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMt4Account } from '../mt-4-account.model';
import { Mt4AccountService } from '../service/mt-4-account.service';

@Component({
  templateUrl: './mt-4-account-delete-dialog.component.html',
})
export class Mt4AccountDeleteDialogComponent {
  mt4Account?: IMt4Account;

  constructor(protected mt4AccountService: Mt4AccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mt4AccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
