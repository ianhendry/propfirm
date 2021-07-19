import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISiteAccount } from '../site-account.model';
import { SiteAccountService } from '../service/site-account.service';

@Component({
  templateUrl: './site-account-delete-dialog.component.html',
})
export class SiteAccountDeleteDialogComponent {
  siteAccount?: ISiteAccount;

  constructor(protected siteAccountService: SiteAccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.siteAccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
