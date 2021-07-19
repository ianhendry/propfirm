import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMt4Trade, getMt4TradeIdentifier } from '../mt-4-trade.model';

export type EntityResponseType = HttpResponse<IMt4Trade>;
export type EntityArrayResponseType = HttpResponse<IMt4Trade[]>;

@Injectable({ providedIn: 'root' })
export class Mt4TradeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mt-4-trades');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mt4Trade: IMt4Trade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mt4Trade);
    return this.http
      .post<IMt4Trade>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mt4Trade: IMt4Trade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mt4Trade);
    return this.http
      .put<IMt4Trade>(`${this.resourceUrl}/${getMt4TradeIdentifier(mt4Trade) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(mt4Trade: IMt4Trade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mt4Trade);
    return this.http
      .patch<IMt4Trade>(`${this.resourceUrl}/${getMt4TradeIdentifier(mt4Trade) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMt4Trade>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMt4Trade[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMt4TradeToCollectionIfMissing(mt4TradeCollection: IMt4Trade[], ...mt4TradesToCheck: (IMt4Trade | null | undefined)[]): IMt4Trade[] {
    const mt4Trades: IMt4Trade[] = mt4TradesToCheck.filter(isPresent);
    if (mt4Trades.length > 0) {
      const mt4TradeCollectionIdentifiers = mt4TradeCollection.map(mt4TradeItem => getMt4TradeIdentifier(mt4TradeItem)!);
      const mt4TradesToAdd = mt4Trades.filter(mt4TradeItem => {
        const mt4TradeIdentifier = getMt4TradeIdentifier(mt4TradeItem);
        if (mt4TradeIdentifier == null || mt4TradeCollectionIdentifiers.includes(mt4TradeIdentifier)) {
          return false;
        }
        mt4TradeCollectionIdentifiers.push(mt4TradeIdentifier);
        return true;
      });
      return [...mt4TradesToAdd, ...mt4TradeCollection];
    }
    return mt4TradeCollection;
  }

  protected convertDateFromClient(mt4Trade: IMt4Trade): IMt4Trade {
    return Object.assign({}, mt4Trade, {
      openTime: mt4Trade.openTime?.isValid() ? mt4Trade.openTime.toJSON() : undefined,
      closeTime: mt4Trade.closeTime?.isValid() ? mt4Trade.closeTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.openTime = res.body.openTime ? dayjs(res.body.openTime) : undefined;
      res.body.closeTime = res.body.closeTime ? dayjs(res.body.closeTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((mt4Trade: IMt4Trade) => {
        mt4Trade.openTime = mt4Trade.openTime ? dayjs(mt4Trade.openTime) : undefined;
        mt4Trade.closeTime = mt4Trade.closeTime ? dayjs(mt4Trade.closeTime) : undefined;
      });
    }
    return res;
  }
}
