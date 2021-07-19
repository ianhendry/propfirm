import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChallengeType } from '../challenge-type.model';
import { ChallengeTypeService } from '../service/challenge-type.service';

@Component({
  templateUrl: './challenge-type-delete-dialog.component.html',
})
export class ChallengeTypeDeleteDialogComponent {
  challengeType?: IChallengeType;

  constructor(protected challengeTypeService: ChallengeTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.challengeTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
