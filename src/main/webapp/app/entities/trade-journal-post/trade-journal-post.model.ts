import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IMt4Trade } from 'app/entities/mt-4-trade/mt-4-trade.model';
import { IUserComment } from 'app/entities/user-comment/user-comment.model';

export interface ITradeJournalPost {
  id?: number;
  postTitle?: string;
  dateAdded?: dayjs.Dayjs;
  thoughtsOnPsychology?: string | null;
  thoughtsOnTradeProcessAccuracy?: string | null;
  thoughtsOnAreasOfStrength?: string | null;
  thoughtsOnAreasForImprovement?: string | null;
  areaOfFocusForTomorrow?: string | null;
  makePublicVisibleOnSite?: boolean | null;
  anyMediaContentType?: string | null;
  anyMedia?: string | null;
  anyImageContentType?: string | null;
  anyImage?: string | null;
  user?: IUser | null;
  mt4Trade?: IMt4Trade | null;
  userComments?: IUserComment[] | null;
}

export class TradeJournalPost implements ITradeJournalPost {
  constructor(
    public id?: number,
    public postTitle?: string,
    public dateAdded?: dayjs.Dayjs,
    public thoughtsOnPsychology?: string | null,
    public thoughtsOnTradeProcessAccuracy?: string | null,
    public thoughtsOnAreasOfStrength?: string | null,
    public thoughtsOnAreasForImprovement?: string | null,
    public areaOfFocusForTomorrow?: string | null,
    public makePublicVisibleOnSite?: boolean | null,
    public anyMediaContentType?: string | null,
    public anyMedia?: string | null,
    public anyImageContentType?: string | null,
    public anyImage?: string | null,
    public user?: IUser | null,
    public mt4Trade?: IMt4Trade | null,
    public userComments?: IUserComment[] | null
  ) {
    this.makePublicVisibleOnSite = this.makePublicVisibleOnSite ?? false;
  }
}

export function getTradeJournalPostIdentifier(tradeJournalPost: ITradeJournalPost): number | undefined {
  return tradeJournalPost.id;
}
