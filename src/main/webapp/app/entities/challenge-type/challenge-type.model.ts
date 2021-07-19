import * as dayjs from 'dayjs';
import { ITradeChallenge } from 'app/entities/trade-challenge/trade-challenge.model';

export interface IChallengeType {
  id?: number;
  challengeTypeName?: string | null;
  priceContentType?: string | null;
  price?: string | null;
  refundAfterComplete?: boolean | null;
  accountSize?: number | null;
  profitTargetPhaseOne?: number | null;
  profitTargetPhaseTwo?: number | null;
  durationDaysPhaseOne?: number | null;
  durationDaysPhaseTwo?: number | null;
  maxDailyDrawdown?: number | null;
  maxTotalDrawDown?: number | null;
  profitSplitRatio?: number | null;
  freeRetry?: boolean | null;
  userBio?: string | null;
  inActive?: boolean | null;
  inActiveDate?: dayjs.Dayjs | null;
  tradeChallenges?: ITradeChallenge[] | null;
}

export class ChallengeType implements IChallengeType {
  constructor(
    public id?: number,
    public challengeTypeName?: string | null,
    public priceContentType?: string | null,
    public price?: string | null,
    public refundAfterComplete?: boolean | null,
    public accountSize?: number | null,
    public profitTargetPhaseOne?: number | null,
    public profitTargetPhaseTwo?: number | null,
    public durationDaysPhaseOne?: number | null,
    public durationDaysPhaseTwo?: number | null,
    public maxDailyDrawdown?: number | null,
    public maxTotalDrawDown?: number | null,
    public profitSplitRatio?: number | null,
    public freeRetry?: boolean | null,
    public userBio?: string | null,
    public inActive?: boolean | null,
    public inActiveDate?: dayjs.Dayjs | null,
    public tradeChallenges?: ITradeChallenge[] | null
  ) {
    this.refundAfterComplete = this.refundAfterComplete ?? false;
    this.freeRetry = this.freeRetry ?? false;
    this.inActive = this.inActive ?? false;
  }
}

export function getChallengeTypeIdentifier(challengeType: IChallengeType): number | undefined {
  return challengeType.id;
}
