import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDailyAnalysisPost } from '../daily-analysis-post.model';
import { DailyAnalysisPostService } from '../service/daily-analysis-post.service';

@Component({
  templateUrl: './daily-analysis-post-delete-dialog.component.html',
})
export class DailyAnalysisPostDeleteDialogComponent {
  dailyAnalysisPost?: IDailyAnalysisPost;

  constructor(protected dailyAnalysisPostService: DailyAnalysisPostService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dailyAnalysisPostService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
