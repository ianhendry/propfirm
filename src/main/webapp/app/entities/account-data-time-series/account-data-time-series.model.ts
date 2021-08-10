import * as dayjs from 'dayjs';
import { IMt4Account } from 'app/entities/mt-4-account/mt-4-account.model';

export interface IAccountDataTimeSeries {
  id?: number;
  dateStamp?: dayjs.Dayjs | null;
  accountBalance?: number | null;
  accountEquity?: number | null;
  accountCredit?: number | null;
  accountFreeMargin?: number | null;
  accountStopoutLevel?: number | null;
  openLots?: number | null;
  openNumberOfTrades?: number | null;
  mt4Account?: IMt4Account | null;
}

export class AccountDataTimeSeries implements IAccountDataTimeSeries {
  constructor(
    public id?: number,
    public dateStamp?: dayjs.Dayjs | null,
    public accountBalance?: number | null,
    public accountEquity?: number | null,
    public accountCredit?: number | null,
    public accountFreeMargin?: number | null,
    public accountStopoutLevel?: number | null,
    public openLots?: number | null,
    public openNumberOfTrades?: number | null,
    public mt4Account?: IMt4Account | null
  ) {}
}

export function getAccountDataTimeSeriesIdentifier(accountDataTimeSeries: IAccountDataTimeSeries): number | undefined {
  return accountDataTimeSeries.id;
}
