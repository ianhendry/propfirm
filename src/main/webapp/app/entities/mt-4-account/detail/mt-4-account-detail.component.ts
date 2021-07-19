import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMt4Account } from '../mt-4-account.model';

@Component({
  selector: 'jhi-mt-4-account-detail',
  templateUrl: './mt-4-account-detail.component.html',
})
export class Mt4AccountDetailComponent implements OnInit {
  mt4Account: IMt4Account | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mt4Account }) => {
      this.mt4Account = mt4Account;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
