import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAccountDataTimeSeries, AccountDataTimeSeries } from '../account-data-time-series.model';
import { AccountDataTimeSeriesService } from '../service/account-data-time-series.service';
import { IMt4Account } from 'app/entities/mt-4-account/mt-4-account.model';
import { Mt4AccountService } from 'app/entities/mt-4-account/service/mt-4-account.service';

@Component({
  selector: 'jhi-account-data-time-series-update',
  templateUrl: './account-data-time-series-update.component.html',
})
export class AccountDataTimeSeriesUpdateComponent implements OnInit {
  isSaving = false;

  mt4AccountsSharedCollection: IMt4Account[] = [];

  editForm = this.fb.group({
    id: [],
    dateStamp: [],
    accountBalance: [],
    accountEquity: [],
    accountCredit: [],
    accountFreeMargin: [],
    accountStopoutLevel: [],
    openLots: [],
    openNumberOfTrades: [],
    mt4Account: [],
  });

  constructor(
    protected accountDataTimeSeriesService: AccountDataTimeSeriesService,
    protected mt4AccountService: Mt4AccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountDataTimeSeries }) => {
      if (accountDataTimeSeries.id === undefined) {
        const today = dayjs().startOf('day');
        accountDataTimeSeries.dateStamp = today;
      }

      this.updateForm(accountDataTimeSeries);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountDataTimeSeries = this.createFromForm();
    if (accountDataTimeSeries.id !== undefined) {
      this.subscribeToSaveResponse(this.accountDataTimeSeriesService.update(accountDataTimeSeries));
    } else {
      this.subscribeToSaveResponse(this.accountDataTimeSeriesService.create(accountDataTimeSeries));
    }
  }

  trackMt4AccountById(index: number, item: IMt4Account): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountDataTimeSeries>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(accountDataTimeSeries: IAccountDataTimeSeries): void {
    this.editForm.patchValue({
      id: accountDataTimeSeries.id,
      dateStamp: accountDataTimeSeries.dateStamp ? accountDataTimeSeries.dateStamp.format(DATE_TIME_FORMAT) : null,
      accountBalance: accountDataTimeSeries.accountBalance,
      accountEquity: accountDataTimeSeries.accountEquity,
      accountCredit: accountDataTimeSeries.accountCredit,
      accountFreeMargin: accountDataTimeSeries.accountFreeMargin,
      accountStopoutLevel: accountDataTimeSeries.accountStopoutLevel,
      openLots: accountDataTimeSeries.openLots,
      openNumberOfTrades: accountDataTimeSeries.openNumberOfTrades,
      mt4Account: accountDataTimeSeries.mt4Account,
    });

    this.mt4AccountsSharedCollection = this.mt4AccountService.addMt4AccountToCollectionIfMissing(
      this.mt4AccountsSharedCollection,
      accountDataTimeSeries.mt4Account
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mt4AccountService
      .query()
      .pipe(map((res: HttpResponse<IMt4Account[]>) => res.body ?? []))
      .pipe(
        map((mt4Accounts: IMt4Account[]) =>
          this.mt4AccountService.addMt4AccountToCollectionIfMissing(mt4Accounts, this.editForm.get('mt4Account')!.value)
        )
      )
      .subscribe((mt4Accounts: IMt4Account[]) => (this.mt4AccountsSharedCollection = mt4Accounts));
  }

  protected createFromForm(): IAccountDataTimeSeries {
    return {
      ...new AccountDataTimeSeries(),
      id: this.editForm.get(['id'])!.value,
      dateStamp: this.editForm.get(['dateStamp'])!.value ? dayjs(this.editForm.get(['dateStamp'])!.value, DATE_TIME_FORMAT) : undefined,
      accountBalance: this.editForm.get(['accountBalance'])!.value,
      accountEquity: this.editForm.get(['accountEquity'])!.value,
      accountCredit: this.editForm.get(['accountCredit'])!.value,
      accountFreeMargin: this.editForm.get(['accountFreeMargin'])!.value,
      accountStopoutLevel: this.editForm.get(['accountStopoutLevel'])!.value,
      openLots: this.editForm.get(['openLots'])!.value,
      openNumberOfTrades: this.editForm.get(['openNumberOfTrades'])!.value,
      mt4Account: this.editForm.get(['mt4Account'])!.value,
    };
  }
}
