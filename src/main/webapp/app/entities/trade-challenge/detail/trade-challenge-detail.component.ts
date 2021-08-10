import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITradeChallenge } from '../trade-challenge.model';

@Component({
  selector: 'jhi-trade-challenge-detail',
  templateUrl: './trade-challenge-detail.component.html',
})
export class TradeChallengeDetailComponent implements OnInit {
  tradeChallenge: ITradeChallenge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tradeChallenge }) => {
      this.tradeChallenge = tradeChallenge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
