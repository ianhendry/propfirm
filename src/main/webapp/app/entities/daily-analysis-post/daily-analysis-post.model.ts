import * as dayjs from 'dayjs';
import { IInstrument } from 'app/entities/instrument/instrument.model';
import { IUser } from 'app/entities/user/user.model';
import { IUserComment } from 'app/entities/user-comment/user-comment.model';

export interface IDailyAnalysisPost {
  id?: number;
  postTitle?: string;
  dateAdded?: dayjs.Dayjs;
  backgroundVolume?: string | null;
  overallThoughts?: string | null;
  weeklyChartContentType?: string | null;
  weeklyChart?: string | null;
  dailyChartContentType?: string | null;
  dailyChart?: string | null;
  oneHrChartContentType?: string | null;
  oneHrChart?: string | null;
  planForToday?: string | null;
  makePublicVisibleOnSite?: boolean | null;
  instrument?: IInstrument | null;
  user?: IUser | null;
  userComments?: IUserComment[] | null;
}

export class DailyAnalysisPost implements IDailyAnalysisPost {
  constructor(
    public id?: number,
    public postTitle?: string,
    public dateAdded?: dayjs.Dayjs,
    public backgroundVolume?: string | null,
    public overallThoughts?: string | null,
    public weeklyChartContentType?: string | null,
    public weeklyChart?: string | null,
    public dailyChartContentType?: string | null,
    public dailyChart?: string | null,
    public oneHrChartContentType?: string | null,
    public oneHrChart?: string | null,
    public planForToday?: string | null,
    public makePublicVisibleOnSite?: boolean | null,
    public instrument?: IInstrument | null,
    public user?: IUser | null,
    public userComments?: IUserComment[] | null
  ) {
    this.makePublicVisibleOnSite = this.makePublicVisibleOnSite ?? false;
  }
}

export function getDailyAnalysisPostIdentifier(dailyAnalysisPost: IDailyAnalysisPost): number | undefined {
  return dailyAnalysisPost.id;
}
