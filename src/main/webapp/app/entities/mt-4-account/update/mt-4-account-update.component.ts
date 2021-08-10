import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMt4Account, Mt4Account } from '../mt-4-account.model';
import { Mt4AccountService } from '../service/mt-4-account.service';

@Component({
  selector: 'jhi-mt-4-account-update',
  templateUrl: './mt-4-account-update.component.html',
})
export class Mt4AccountUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    accountType: [],
    accountBroker: [],
    accountLogin: [],
    accountPassword: [],
    accountInvestorLogin: [],
    accountInvestorPassword: [],
    accountReal: [],
    accountInfoDouble: [],
    accountInfoInteger: [],
    accountInfoString: [],
    accountBalance: [],
    accountCredit: [],
    accountCompany: [],
    accountCurrency: [],
    accountEquity: [],
    accountFreeMargin: [],
    accountFreeMarginCheck: [],
    accountFreeMarginMode: [],
    accountLeverage: [],
    accountMargin: [],
    accountName: [],
    accountNumber: [],
    accountProfit: [],
    accountServer: [],
    accountStopoutLevel: [],
    accountStopoutMode: [],
    inActive: [],
    inActiveDate: [],
  });

  constructor(protected mt4AccountService: Mt4AccountService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mt4Account }) => {
      if (mt4Account.id === undefined) {
        const today = dayjs().startOf('day');
        mt4Account.inActiveDate = today;
      }

      this.updateForm(mt4Account);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mt4Account = this.createFromForm();
    if (mt4Account.id !== undefined) {
      this.subscribeToSaveResponse(this.mt4AccountService.update(mt4Account));
    } else {
      this.subscribeToSaveResponse(this.mt4AccountService.create(mt4Account));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMt4Account>>): void {
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

  protected updateForm(mt4Account: IMt4Account): void {
    this.editForm.patchValue({
      id: mt4Account.id,
      accountType: mt4Account.accountType,
      accountBroker: mt4Account.accountBroker,
      accountLogin: mt4Account.accountLogin,
      accountPassword: mt4Account.accountPassword,
      accountInvestorLogin: mt4Account.accountInvestorLogin,
      accountInvestorPassword: mt4Account.accountInvestorPassword,
      accountReal: mt4Account.accountReal,
      accountInfoDouble: mt4Account.accountInfoDouble,
      accountInfoInteger: mt4Account.accountInfoInteger,
      accountInfoString: mt4Account.accountInfoString,
      accountBalance: mt4Account.accountBalance,
      accountCredit: mt4Account.accountCredit,
      accountCompany: mt4Account.accountCompany,
      accountCurrency: mt4Account.accountCurrency,
      accountEquity: mt4Account.accountEquity,
      accountFreeMargin: mt4Account.accountFreeMargin,
      accountFreeMarginCheck: mt4Account.accountFreeMarginCheck,
      accountFreeMarginMode: mt4Account.accountFreeMarginMode,
      accountLeverage: mt4Account.accountLeverage,
      accountMargin: mt4Account.accountMargin,
      accountName: mt4Account.accountName,
      accountNumber: mt4Account.accountNumber,
      accountProfit: mt4Account.accountProfit,
      accountServer: mt4Account.accountServer,
      accountStopoutLevel: mt4Account.accountStopoutLevel,
      accountStopoutMode: mt4Account.accountStopoutMode,
      inActive: mt4Account.inActive,
      inActiveDate: mt4Account.inActiveDate ? mt4Account.inActiveDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IMt4Account {
    return {
      ...new Mt4Account(),
      id: this.editForm.get(['id'])!.value,
      accountType: this.editForm.get(['accountType'])!.value,
      accountBroker: this.editForm.get(['accountBroker'])!.value,
      accountLogin: this.editForm.get(['accountLogin'])!.value,
      accountPassword: this.editForm.get(['accountPassword'])!.value,
      accountInvestorLogin: this.editForm.get(['accountInvestorLogin'])!.value,
      accountInvestorPassword: this.editForm.get(['accountInvestorPassword'])!.value,
      accountReal: this.editForm.get(['accountReal'])!.value,
      accountInfoDouble: this.editForm.get(['accountInfoDouble'])!.value,
      accountInfoInteger: this.editForm.get(['accountInfoInteger'])!.value,
      accountInfoString: this.editForm.get(['accountInfoString'])!.value,
      accountBalance: this.editForm.get(['accountBalance'])!.value,
      accountCredit: this.editForm.get(['accountCredit'])!.value,
      accountCompany: this.editForm.get(['accountCompany'])!.value,
      accountCurrency: this.editForm.get(['accountCurrency'])!.value,
      accountEquity: this.editForm.get(['accountEquity'])!.value,
      accountFreeMargin: this.editForm.get(['accountFreeMargin'])!.value,
      accountFreeMarginCheck: this.editForm.get(['accountFreeMarginCheck'])!.value,
      accountFreeMarginMode: this.editForm.get(['accountFreeMarginMode'])!.value,
      accountLeverage: this.editForm.get(['accountLeverage'])!.value,
      accountMargin: this.editForm.get(['accountMargin'])!.value,
      accountName: this.editForm.get(['accountName'])!.value,
      accountNumber: this.editForm.get(['accountNumber'])!.value,
      accountProfit: this.editForm.get(['accountProfit'])!.value,
      accountServer: this.editForm.get(['accountServer'])!.value,
      accountStopoutLevel: this.editForm.get(['accountStopoutLevel'])!.value,
      accountStopoutMode: this.editForm.get(['accountStopoutMode'])!.value,
      inActive: this.editForm.get(['inActive'])!.value,
      inActiveDate: this.editForm.get(['inActiveDate'])!.value
        ? dayjs(this.editForm.get(['inActiveDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
