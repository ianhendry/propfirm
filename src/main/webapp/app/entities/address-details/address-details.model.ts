import * as dayjs from 'dayjs';
import { ISiteAccount } from 'app/entities/site-account/site-account.model';

export interface IAddressDetails {
  id?: number;
  contactName?: string | null;
  address1?: string | null;
  address2?: string | null;
  address3?: string | null;
  address4?: string | null;
  address5?: string | null;
  address6?: string | null;
  dialCode?: string | null;
  phoneNumber?: string | null;
  messengerId?: string | null;
  dateAdded?: dayjs.Dayjs | null;
  inActive?: boolean | null;
  inActiveDate?: dayjs.Dayjs | null;
  siteAccounts?: ISiteAccount[] | null;
}

export class AddressDetails implements IAddressDetails {
  constructor(
    public id?: number,
    public contactName?: string | null,
    public address1?: string | null,
    public address2?: string | null,
    public address3?: string | null,
    public address4?: string | null,
    public address5?: string | null,
    public address6?: string | null,
    public dialCode?: string | null,
    public phoneNumber?: string | null,
    public messengerId?: string | null,
    public dateAdded?: dayjs.Dayjs | null,
    public inActive?: boolean | null,
    public inActiveDate?: dayjs.Dayjs | null,
    public siteAccounts?: ISiteAccount[] | null
  ) {
    this.inActive = this.inActive ?? false;
  }
}

export function getAddressDetailsIdentifier(addressDetails: IAddressDetails): number | undefined {
  return addressDetails.id;
}
