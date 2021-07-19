import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChallengeType } from '../challenge-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-challenge-type-detail',
  templateUrl: './challenge-type-detail.component.html',
})
export class ChallengeTypeDetailComponent implements OnInit {
  challengeType: IChallengeType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ challengeType }) => {
      this.challengeType = challengeType;
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
