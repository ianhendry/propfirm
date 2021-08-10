import * as dayjs from 'dayjs';
import { ITradeChallenge } from 'app/entities/trade-challenge/trade-challenge.model';
import { IMt4Trade } from 'app/entities/mt-4-trade/mt-4-trade.model';
import { IAccountDataTimeSeries } from 'app/entities/account-data-time-series/account-data-time-series.model';
import { ACCOUNTTYPE } from 'app/entities/enumerations/accounttype.model';
import { BROKER } from 'app/entities/enumerations/broker.model';

export interface IMt4Account {
  id?: number;
  accountType?: ACCOUNTTYPE | null;
  accountBroker?: BROKER | null;
  accountLogin?: string | null;
  accountPassword?: string | null;
  accountInvestorLogin?: string | null;
  accountInvestorPassword?: string | null;
  accountReal?: boolean | null;
  accountInfoDouble?: number | null;
  accountInfoInteger?: number | null;
  accountInfoString?: string | null;
  accountBalance?: number | null;
  accountCredit?: number | null;
  accountCompany?: string | null;
  accountCurrency?: string | null;
  accountEquity?: number | null;
  accountFreeMargin?: number | null;
  accountFreeMarginCheck?: number | null;
  accountFreeMarginMode?: number | null;
  accountLeverage?: number | null;
  accountMargin?: number | null;
  accountName?: string | null;
  accountNumber?: number | null;
  accountProfit?: number | null;
  accountServer?: string | null;
  accountStopoutLevel?: number | null;
  accountStopoutMode?: number | null;
  inActive?: boolean | null;
  inActiveDate?: dayjs.Dayjs | null;
  tradeChallenge?: ITradeChallenge | null;
  mt4Trades?: IMt4Trade[] | null;
  accountDataTimeSeries?: IAccountDataTimeSeries[] | null;
}

export class Mt4Account implements IMt4Account {
  constructor(
    public id?: number,
    public accountType?: ACCOUNTTYPE | null,
    public accountBroker?: BROKER | null,
    public accountLogin?: string | null,
    public accountPassword?: string | null,
    public accountInvestorLogin?: string | null,
    public accountInvestorPassword?: string | null,
    public accountReal?: boolean | null,
    public accountInfoDouble?: number | null,
    public accountInfoInteger?: number | null,
    public accountInfoString?: string | null,
    public accountBalance?: number | null,
    public accountCredit?: number | null,
    public accountCompany?: string | null,
    public accountCurrency?: string | null,
    public accountEquity?: number | null,
    public accountFreeMargin?: number | null,
    public accountFreeMarginCheck?: number | null,
    public accountFreeMarginMode?: number | null,
    public accountLeverage?: number | null,
    public accountMargin?: number | null,
    public accountName?: string | null,
    public accountNumber?: number | null,
    public accountProfit?: number | null,
    public accountServer?: string | null,
    public accountStopoutLevel?: number | null,
    public accountStopoutMode?: number | null,
    public inActive?: boolean | null,
    public inActiveDate?: dayjs.Dayjs | null,
    public tradeChallenge?: ITradeChallenge | null,
    public mt4Trades?: IMt4Trade[] | null,
    public accountDataTimeSeries?: IAccountDataTimeSeries[] | null
  ) {
    this.accountReal = this.accountReal ?? false;
    this.inActive = this.inActive ?? false;
  }
}

export function getMt4AccountIdentifier(mt4Account: IMt4Account): number | undefined {
  return mt4Account.id;
}
