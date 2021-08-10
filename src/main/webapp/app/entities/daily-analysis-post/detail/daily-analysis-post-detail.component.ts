import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDailyAnalysisPost } from '../daily-analysis-post.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-daily-analysis-post-detail',
  templateUrl: './daily-analysis-post-detail.component.html',
})
export class DailyAnalysisPostDetailComponent implements OnInit {
  dailyAnalysisPost: IDailyAnalysisPost | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dailyAnalysisPost }) => {
      this.dailyAnalysisPost = dailyAnalysisPost;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
