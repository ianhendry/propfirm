import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMt4Trade } from '../mt-4-trade.model';

@Component({
  selector: 'jhi-mt-4-trade-detail',
  templateUrl: './mt-4-trade-detail.component.html',
})
export class Mt4TradeDetailComponent implements OnInit {
  mt4Trade: IMt4Trade | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mt4Trade }) => {
      this.mt4Trade = mt4Trade;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
