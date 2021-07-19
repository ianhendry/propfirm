import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccountDataTimeSeries, getAccountDataTimeSeriesIdentifier } from '../account-data-time-series.model';

export type EntityResponseType = HttpResponse<IAccountDataTimeSeries>;
export type EntityArrayResponseType = HttpResponse<IAccountDataTimeSeries[]>;

@Injectable({ providedIn: 'root' })
export class AccountDataTimeSeriesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-data-time-series');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accountDataTimeSeries: IAccountDataTimeSeries): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountDataTimeSeries);
    return this.http
      .post<IAccountDataTimeSeries>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(accountDataTimeSeries: IAccountDataTimeSeries): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountDataTimeSeries);
    return this.http
      .put<IAccountDataTimeSeries>(`${this.resourceUrl}/${getAccountDataTimeSeriesIdentifier(accountDataTimeSeries) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(accountDataTimeSeries: IAccountDataTimeSeries): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountDataTimeSeries);
    return this.http
      .patch<IAccountDataTimeSeries>(`${this.resourceUrl}/${getAccountDataTimeSeriesIdentifier(accountDataTimeSeries) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAccountDataTimeSeries>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccountDataTimeSeries[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAccountDataTimeSeriesToCollectionIfMissing(
    accountDataTimeSeriesCollection: IAccountDataTimeSeries[],
    ...accountDataTimeSeriesToCheck: (IAccountDataTimeSeries | null | undefined)[]
  ): IAccountDataTimeSeries[] {
    const accountDataTimeSeries: IAccountDataTimeSeries[] = accountDataTimeSeriesToCheck.filter(isPresent);
    if (accountDataTimeSeries.length > 0) {
      const accountDataTimeSeriesCollectionIdentifiers = accountDataTimeSeriesCollection.map(
        accountDataTimeSeriesItem => getAccountDataTimeSeriesIdentifier(accountDataTimeSeriesItem)!
      );
      const accountDataTimeSeriesToAdd = accountDataTimeSeries.filter(accountDataTimeSeriesItem => {
        const accountDataTimeSeriesIdentifier = getAccountDataTimeSeriesIdentifier(accountDataTimeSeriesItem);
        if (
          accountDataTimeSeriesIdentifier == null ||
          accountDataTimeSeriesCollectionIdentifiers.includes(accountDataTimeSeriesIdentifier)
        ) {
          return false;
        }
        accountDataTimeSeriesCollectionIdentifiers.push(accountDataTimeSeriesIdentifier);
        return true;
      });
      return [...accountDataTimeSeriesToAdd, ...accountDataTimeSeriesCollection];
    }
    return accountDataTimeSeriesCollection;
  }

  protected convertDateFromClient(accountDataTimeSeries: IAccountDataTimeSeries): IAccountDataTimeSeries {
    return Object.assign({}, accountDataTimeSeries, {
      dateStamp: accountDataTimeSeries.dateStamp?.isValid() ? accountDataTimeSeries.dateStamp.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateStamp = res.body.dateStamp ? dayjs(res.body.dateStamp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((accountDataTimeSeries: IAccountDataTimeSeries) => {
        accountDataTimeSeries.dateStamp = accountDataTimeSeries.dateStamp ? dayjs(accountDataTimeSeries.dateStamp) : undefined;
      });
    }
    return res;
  }
}
