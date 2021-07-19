import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountDataTimeSeries } from '../account-data-time-series.model';

@Component({
  selector: 'jhi-account-data-time-series-detail',
  templateUrl: './account-data-time-series-detail.component.html',
})
export class AccountDataTimeSeriesDetailComponent implements OnInit {
  accountDataTimeSeries: IAccountDataTimeSeries | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountDataTimeSeries }) => {
      this.accountDataTimeSeries = accountDataTimeSeries;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
