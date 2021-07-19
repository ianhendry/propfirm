import * as dayjs from 'dayjs';
import { IMt4Trade } from 'app/entities/mt-4-trade/mt-4-trade.model';
import { IDailyAnalysisPost } from 'app/entities/daily-analysis-post/daily-analysis-post.model';
import { INSTRUMENTTYPE } from 'app/entities/enumerations/instrumenttype.model';

export interface IInstrument {
  id?: number;
  ticker?: string | null;
  instrumentType?: INSTRUMENTTYPE | null;
  exchange?: string | null;
  averageSpread?: number | null;
  tradeRestrictions?: string | null;
  inActive?: boolean | null;
  inActiveDate?: dayjs.Dayjs | null;
  mt4Trades?: IMt4Trade[] | null;
  dailyAnalysisPosts?: IDailyAnalysisPost[] | null;
}

export class Instrument implements IInstrument {
  constructor(
    public id?: number,
    public ticker?: string | null,
    public instrumentType?: INSTRUMENTTYPE | null,
    public exchange?: string | null,
    public averageSpread?: number | null,
    public tradeRestrictions?: string | null,
    public inActive?: boolean | null,
    public inActiveDate?: dayjs.Dayjs | null,
    public mt4Trades?: IMt4Trade[] | null,
    public dailyAnalysisPosts?: IDailyAnalysisPost[] | null
  ) {
    this.inActive = this.inActive ?? false;
  }
}

export function getInstrumentIdentifier(instrument: IInstrument): number | undefined {
  return instrument.id;
}
