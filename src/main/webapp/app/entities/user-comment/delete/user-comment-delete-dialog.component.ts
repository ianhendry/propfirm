import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserComment } from '../user-comment.model';
import { UserCommentService } from '../service/user-comment.service';

@Component({
  templateUrl: './user-comment-delete-dialog.component.html',
})
export class UserCommentDeleteDialogComponent {
  userComment?: IUserComment;

  constructor(protected userCommentService: UserCommentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userCommentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
