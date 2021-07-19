import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISiteAccount, getSiteAccountIdentifier } from '../site-account.model';

export type EntityResponseType = HttpResponse<ISiteAccount>;
export type EntityArrayResponseType = HttpResponse<ISiteAccount[]>;

@Injectable({ providedIn: 'root' })
export class SiteAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/site-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(siteAccount: ISiteAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(siteAccount);
    return this.http
      .post<ISiteAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(siteAccount: ISiteAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(siteAccount);
    return this.http
      .put<ISiteAccount>(`${this.resourceUrl}/${getSiteAccountIdentifier(siteAccount) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(siteAccount: ISiteAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(siteAccount);
    return this.http
      .patch<ISiteAccount>(`${this.resourceUrl}/${getSiteAccountIdentifier(siteAccount) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISiteAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISiteAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSiteAccountToCollectionIfMissing(
    siteAccountCollection: ISiteAccount[],
    ...siteAccountsToCheck: (ISiteAccount | null | undefined)[]
  ): ISiteAccount[] {
    const siteAccounts: ISiteAccount[] = siteAccountsToCheck.filter(isPresent);
    if (siteAccounts.length > 0) {
      const siteAccountCollectionIdentifiers = siteAccountCollection.map(siteAccountItem => getSiteAccountIdentifier(siteAccountItem)!);
      const siteAccountsToAdd = siteAccounts.filter(siteAccountItem => {
        const siteAccountIdentifier = getSiteAccountIdentifier(siteAccountItem);
        if (siteAccountIdentifier == null || siteAccountCollectionIdentifiers.includes(siteAccountIdentifier)) {
          return false;
        }
        siteAccountCollectionIdentifiers.push(siteAccountIdentifier);
        return true;
      });
      return [...siteAccountsToAdd, ...siteAccountCollection];
    }
    return siteAccountCollection;
  }

  protected convertDateFromClient(siteAccount: ISiteAccount): ISiteAccount {
    return Object.assign({}, siteAccount, {
      inActiveDate: siteAccount.inActiveDate?.isValid() ? siteAccount.inActiveDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.inActiveDate = res.body.inActiveDate ? dayjs(res.body.inActiveDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((siteAccount: ISiteAccount) => {
        siteAccount.inActiveDate = siteAccount.inActiveDate ? dayjs(siteAccount.inActiveDate) : undefined;
      });
    }
    return res;
  }
}
