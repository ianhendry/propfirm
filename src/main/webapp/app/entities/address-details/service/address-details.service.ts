import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAddressDetails, getAddressDetailsIdentifier } from '../address-details.model';

export type EntityResponseType = HttpResponse<IAddressDetails>;
export type EntityArrayResponseType = HttpResponse<IAddressDetails[]>;

@Injectable({ providedIn: 'root' })
export class AddressDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/address-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(addressDetails: IAddressDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(addressDetails);
    return this.http
      .post<IAddressDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(addressDetails: IAddressDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(addressDetails);
    return this.http
      .put<IAddressDetails>(`${this.resourceUrl}/${getAddressDetailsIdentifier(addressDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(addressDetails: IAddressDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(addressDetails);
    return this.http
      .patch<IAddressDetails>(`${this.resourceUrl}/${getAddressDetailsIdentifier(addressDetails) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAddressDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAddressDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAddressDetailsToCollectionIfMissing(
    addressDetailsCollection: IAddressDetails[],
    ...addressDetailsToCheck: (IAddressDetails | null | undefined)[]
  ): IAddressDetails[] {
    const addressDetails: IAddressDetails[] = addressDetailsToCheck.filter(isPresent);
    if (addressDetails.length > 0) {
      const addressDetailsCollectionIdentifiers = addressDetailsCollection.map(
        addressDetailsItem => getAddressDetailsIdentifier(addressDetailsItem)!
      );
      const addressDetailsToAdd = addressDetails.filter(addressDetailsItem => {
        const addressDetailsIdentifier = getAddressDetailsIdentifier(addressDetailsItem);
        if (addressDetailsIdentifier == null || addressDetailsCollectionIdentifiers.includes(addressDetailsIdentifier)) {
          return false;
        }
        addressDetailsCollectionIdentifiers.push(addressDetailsIdentifier);
        return true;
      });
      return [...addressDetailsToAdd, ...addressDetailsCollection];
    }
    return addressDetailsCollection;
  }

  protected convertDateFromClient(addressDetails: IAddressDetails): IAddressDetails {
    return Object.assign({}, addressDetails, {
      dateAdded: addressDetails.dateAdded?.isValid() ? addressDetails.dateAdded.toJSON() : undefined,
      inActiveDate: addressDetails.inActiveDate?.isValid() ? addressDetails.inActiveDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateAdded = res.body.dateAdded ? dayjs(res.body.dateAdded) : undefined;
      res.body.inActiveDate = res.body.inActiveDate ? dayjs(res.body.inActiveDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((addressDetails: IAddressDetails) => {
        addressDetails.dateAdded = addressDetails.dateAdded ? dayjs(addressDetails.dateAdded) : undefined;
        addressDetails.inActiveDate = addressDetails.inActiveDate ? dayjs(addressDetails.inActiveDate) : undefined;
      });
    }
    return res;
  }
}
