import * as dayjs from 'dayjs';
import { ITradeJournalPost } from 'app/entities/trade-journal-post/trade-journal-post.model';
import { IDailyAnalysisPost } from 'app/entities/daily-analysis-post/daily-analysis-post.model';
import { IUser } from 'app/entities/user/user.model';

export interface IUserComment {
  id?: number;
  commentTitle?: string;
  commentBody?: string | null;
  commentMediaContentType?: string | null;
  commentMedia?: string | null;
  dateAdded?: dayjs.Dayjs;
  makePublicVisibleOnSite?: boolean | null;
  tradeJournalPost?: ITradeJournalPost | null;
  dailyAnalysisPost?: IDailyAnalysisPost | null;
  user?: IUser | null;
}

export class UserComment implements IUserComment {
  constructor(
    public id?: number,
    public commentTitle?: string,
    public commentBody?: string | null,
    public commentMediaContentType?: string | null,
    public commentMedia?: string | null,
    public dateAdded?: dayjs.Dayjs,
    public makePublicVisibleOnSite?: boolean | null,
    public tradeJournalPost?: ITradeJournalPost | null,
    public dailyAnalysisPost?: IDailyAnalysisPost | null,
    public user?: IUser | null
  ) {
    this.makePublicVisibleOnSite = this.makePublicVisibleOnSite ?? false;
  }
}

export function getUserCommentIdentifier(userComment: IUserComment): number | undefined {
  return userComment.id;
}
