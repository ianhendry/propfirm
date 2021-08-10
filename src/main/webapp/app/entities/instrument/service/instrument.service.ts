import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInstrument, getInstrumentIdentifier } from '../instrument.model';

export type EntityResponseType = HttpResponse<IInstrument>;
export type EntityArrayResponseType = HttpResponse<IInstrument[]>;

@Injectable({ providedIn: 'root' })
export class InstrumentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/instruments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(instrument: IInstrument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(instrument);
    return this.http
      .post<IInstrument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(instrument: IInstrument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(instrument);
    return this.http
      .put<IInstrument>(`${this.resourceUrl}/${getInstrumentIdentifier(instrument) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(instrument: IInstrument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(instrument);
    return this.http
      .patch<IInstrument>(`${this.resourceUrl}/${getInstrumentIdentifier(instrument) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInstrument>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInstrument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInstrumentToCollectionIfMissing(
    instrumentCollection: IInstrument[],
    ...instrumentsToCheck: (IInstrument | null | undefined)[]
  ): IInstrument[] {
    const instruments: IInstrument[] = instrumentsToCheck.filter(isPresent);
    if (instruments.length > 0) {
      const instrumentCollectionIdentifiers = instrumentCollection.map(instrumentItem => getInstrumentIdentifier(instrumentItem)!);
      const instrumentsToAdd = instruments.filter(instrumentItem => {
        const instrumentIdentifier = getInstrumentIdentifier(instrumentItem);
        if (instrumentIdentifier == null || instrumentCollectionIdentifiers.includes(instrumentIdentifier)) {
          return false;
        }
        instrumentCollectionIdentifiers.push(instrumentIdentifier);
        return true;
      });
      return [...instrumentsToAdd, ...instrumentCollection];
    }
    return instrumentCollection;
  }

  protected convertDateFromClient(instrument: IInstrument): IInstrument {
    return Object.assign({}, instrument, {
      inActiveDate: instrument.inActiveDate?.isValid() ? instrument.inActiveDate.toJSON() : undefined,
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
      res.body.forEach((instrument: IInstrument) => {
        instrument.inActiveDate = instrument.inActiveDate ? dayjs(instrument.inActiveDate) : undefined;
      });
    }
    return res;
  }
}
