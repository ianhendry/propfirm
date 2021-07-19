import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccountDataTimeSeries } from '../account-data-time-series.model';
import { AccountDataTimeSeriesService } from '../service/account-data-time-series.service';
import { AccountDataTimeSeriesDeleteDialogComponent } from '../delete/account-data-time-series-delete-dialog.component';

@Component({
  selector: 'jhi-account-data-time-series',
  templateUrl: './account-data-time-series.component.html',
})
export class AccountDataTimeSeriesComponent implements OnInit {
  accountDataTimeSeries?: IAccountDataTimeSeries[];
  isLoading = false;

  constructor(protected accountDataTimeSeriesService: AccountDataTimeSeriesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.accountDataTimeSeriesService.query().subscribe(
      (res: HttpResponse<IAccountDataTimeSeries[]>) => {
        this.isLoading = false;
        this.accountDataTimeSeries = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAccountDataTimeSeries): number {
    return item.id!;
  }

  delete(accountDataTimeSeries: IAccountDataTimeSeries): void {
    const modalRef = this.modalService.open(AccountDataTimeSeriesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.accountDataTimeSeries = accountDataTimeSeries;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
