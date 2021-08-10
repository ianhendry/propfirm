import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IAddressDetails } from 'app/entities/address-details/address-details.model';
import { ITradeChallenge } from 'app/entities/trade-challenge/trade-challenge.model';

export interface ISiteAccount {
  id?: number;
  accountName?: string | null;
  userPictureContentType?: string | null;
  userPicture?: string | null;
  userBio?: string | null;
  inActive?: boolean | null;
  inActiveDate?: dayjs.Dayjs | null;
  user?: IUser | null;
  addressDetails?: IAddressDetails | null;
  tradeChallenges?: ITradeChallenge[] | null;
}

export class SiteAccount implements ISiteAccount {
  constructor(
    public id?: number,
    public accountName?: string | null,
    public userPictureContentType?: string | null,
    public userPicture?: string | null,
    public userBio?: string | null,
    public inActive?: boolean | null,
    public inActiveDate?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public addressDetails?: IAddressDetails | null,
    public tradeChallenges?: ITradeChallenge[] | null
  ) {
    this.inActive = this.inActive ?? false;
  }
}

export function getSiteAccountIdentifier(siteAccount: ISiteAccount): number | undefined {
  return siteAccount.id;
}
