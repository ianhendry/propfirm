import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInstrument } from '../instrument.model';
import { InstrumentService } from '../service/instrument.service';

@Component({
  templateUrl: './instrument-delete-dialog.component.html',
})
export class InstrumentDeleteDialogComponent {
  instrument?: IInstrument;

  constructor(protected instrumentService: InstrumentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.instrumentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
