import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMt4Account, getMt4AccountIdentifier } from '../mt-4-account.model';

export type EntityResponseType = HttpResponse<IMt4Account>;
export type EntityArrayResponseType = HttpResponse<IMt4Account[]>;

@Injectable({ providedIn: 'root' })
export class Mt4AccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mt-4-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mt4Account: IMt4Account): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mt4Account);
    return this.http
      .post<IMt4Account>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mt4Account: IMt4Account): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mt4Account);
    return this.http
      .put<IMt4Account>(`${this.resourceUrl}/${getMt4AccountIdentifier(mt4Account) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(mt4Account: IMt4Account): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mt4Account);
    return this.http
      .patch<IMt4Account>(`${this.resourceUrl}/${getMt4AccountIdentifier(mt4Account) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMt4Account>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMt4Account[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMt4AccountToCollectionIfMissing(
    mt4AccountCollection: IMt4Account[],
    ...mt4AccountsToCheck: (IMt4Account | null | undefined)[]
  ): IMt4Account[] {
    const mt4Accounts: IMt4Account[] = mt4AccountsToCheck.filter(isPresent);
    if (mt4Accounts.length > 0) {
      const mt4AccountCollectionIdentifiers = mt4AccountCollection.map(mt4AccountItem => getMt4AccountIdentifier(mt4AccountItem)!);
      const mt4AccountsToAdd = mt4Accounts.filter(mt4AccountItem => {
        const mt4AccountIdentifier = getMt4AccountIdentifier(mt4AccountItem);
        if (mt4AccountIdentifier == null || mt4AccountCollectionIdentifiers.includes(mt4AccountIdentifier)) {
          return false;
        }
        mt4AccountCollectionIdentifiers.push(mt4AccountIdentifier);
        return true;
      });
      return [...mt4AccountsToAdd, ...mt4AccountCollection];
    }
    return mt4AccountCollection;
  }

  protected convertDateFromClient(mt4Account: IMt4Account): IMt4Account {
    return Object.assign({}, mt4Account, {
      inActiveDate: mt4Account.inActiveDate?.isValid() ? mt4Account.inActiveDate.toJSON() : undefined,
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
      res.body.forEach((mt4Account: IMt4Account) => {
        mt4Account.inActiveDate = mt4Account.inActiveDate ? dayjs(mt4Account.inActiveDate) : undefined;
      });
    }
    return res;
  }
}
