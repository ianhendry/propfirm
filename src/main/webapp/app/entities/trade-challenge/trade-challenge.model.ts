import * as dayjs from 'dayjs';
import { IMt4Account } from 'app/entities/mt-4-account/mt-4-account.model';
import { ISiteAccount } from 'app/entities/site-account/site-account.model';
import { IChallengeType } from 'app/entities/challenge-type/challenge-type.model';

export interface ITradeChallenge {
  id?: number;
  tradeChallengeName?: string | null;
  startDate?: dayjs.Dayjs | null;
  runningMaxTotalDrawdown?: number | null;
  runningMaxDailyDrawdown?: number | null;
  rulesViolated?: boolean | null;
  ruleViolated?: string | null;
  ruleViolatedDate?: dayjs.Dayjs | null;
  maxTotalDrawdown?: number | null;
  maxDailyDrawdown?: number | null;
  lastDailyResetDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  mt4Account?: IMt4Account | null;
  siteAccount?: ISiteAccount | null;
  challengeType?: IChallengeType | null;
}

export class TradeChallenge implements ITradeChallenge {
  constructor(
    public id?: number,
    public tradeChallengeName?: string | null,
    public startDate?: dayjs.Dayjs | null,
    public runningMaxTotalDrawdown?: number | null,
    public runningMaxDailyDrawdown?: number | null,
    public rulesViolated?: boolean | null,
    public ruleViolated?: string | null,
    public ruleViolatedDate?: dayjs.Dayjs | null,
    public maxTotalDrawdown?: number | null,
    public maxDailyDrawdown?: number | null,
    public lastDailyResetDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public mt4Account?: IMt4Account | null,
    public siteAccount?: ISiteAccount | null,
    public challengeType?: IChallengeType | null
  ) {
    this.rulesViolated = this.rulesViolated ?? false;
  }
}

export function getTradeChallengeIdentifier(tradeChallenge: ITradeChallenge): number | undefined {
  return tradeChallenge.id;
}
