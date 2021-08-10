import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITradeChallenge, getTradeChallengeIdentifier } from '../trade-challenge.model';

export type EntityResponseType = HttpResponse<ITradeChallenge>;
export type EntityArrayResponseType = HttpResponse<ITradeChallenge[]>;

@Injectable({ providedIn: 'root' })
export class TradeChallengeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trade-challenges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tradeChallenge: ITradeChallenge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeChallenge);
    return this.http
      .post<ITradeChallenge>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tradeChallenge: ITradeChallenge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeChallenge);
    return this.http
      .put<ITradeChallenge>(`${this.resourceUrl}/${getTradeChallengeIdentifier(tradeChallenge) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tradeChallenge: ITradeChallenge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeChallenge);
    return this.http
      .patch<ITradeChallenge>(`${this.resourceUrl}/${getTradeChallengeIdentifier(tradeChallenge) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITradeChallenge>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITradeChallenge[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTradeChallengeToCollectionIfMissing(
    tradeChallengeCollection: ITradeChallenge[],
    ...tradeChallengesToCheck: (ITradeChallenge | null | undefined)[]
  ): ITradeChallenge[] {
    const tradeChallenges: ITradeChallenge[] = tradeChallengesToCheck.filter(isPresent);
    if (tradeChallenges.length > 0) {
      const tradeChallengeCollectionIdentifiers = tradeChallengeCollection.map(
        tradeChallengeItem => getTradeChallengeIdentifier(tradeChallengeItem)!
      );
      const tradeChallengesToAdd = tradeChallenges.filter(tradeChallengeItem => {
        const tradeChallengeIdentifier = getTradeChallengeIdentifier(tradeChallengeItem);
        if (tradeChallengeIdentifier == null || tradeChallengeCollectionIdentifiers.includes(tradeChallengeIdentifier)) {
          return false;
        }
        tradeChallengeCollectionIdentifiers.push(tradeChallengeIdentifier);
        return true;
      });
      return [...tradeChallengesToAdd, ...tradeChallengeCollection];
    }
    return tradeChallengeCollection;
  }

  protected convertDateFromClient(tradeChallenge: ITradeChallenge): ITradeChallenge {
    return Object.assign({}, tradeChallenge, {
      startDate: tradeChallenge.startDate?.isValid() ? tradeChallenge.startDate.toJSON() : undefined,
      ruleViolatedDate: tradeChallenge.ruleViolatedDate?.isValid() ? tradeChallenge.ruleViolatedDate.toJSON() : undefined,
      lastDailyResetDate: tradeChallenge.lastDailyResetDate?.isValid() ? tradeChallenge.lastDailyResetDate.toJSON() : undefined,
      endDate: tradeChallenge.endDate?.isValid() ? tradeChallenge.endDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.ruleViolatedDate = res.body.ruleViolatedDate ? dayjs(res.body.ruleViolatedDate) : undefined;
      res.body.lastDailyResetDate = res.body.lastDailyResetDate ? dayjs(res.body.lastDailyResetDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tradeChallenge: ITradeChallenge) => {
        tradeChallenge.startDate = tradeChallenge.startDate ? dayjs(tradeChallenge.startDate) : undefined;
        tradeChallenge.ruleViolatedDate = tradeChallenge.ruleViolatedDate ? dayjs(tradeChallenge.ruleViolatedDate) : undefined;
        tradeChallenge.lastDailyResetDate = tradeChallenge.lastDailyResetDate ? dayjs(tradeChallenge.lastDailyResetDate) : undefined;
        tradeChallenge.endDate = tradeChallenge.endDate ? dayjs(tradeChallenge.endDate) : undefined;
      });
    }
    return res;
  }
}
