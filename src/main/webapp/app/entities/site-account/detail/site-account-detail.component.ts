import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISiteAccount } from '../site-account.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-site-account-detail',
  templateUrl: './site-account-detail.component.html',
})
export class SiteAccountDetailComponent implements OnInit {
  siteAccount: ISiteAccount | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ siteAccount }) => {
      this.siteAccount = siteAccount;
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
