import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccountDataTimeSeries } from '../account-data-time-series.model';
import { AccountDataTimeSeriesService } from '../service/account-data-time-series.service';

@Component({
  templateUrl: './account-data-time-series-delete-dialog.component.html',
})
export class AccountDataTimeSeriesDeleteDialogComponent {
  accountDataTimeSeries?: IAccountDataTimeSeries;

  constructor(protected accountDataTimeSeriesService: AccountDataTimeSeriesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountDataTimeSeriesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
