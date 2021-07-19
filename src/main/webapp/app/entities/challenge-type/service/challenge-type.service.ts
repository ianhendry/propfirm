import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChallengeType, getChallengeTypeIdentifier } from '../challenge-type.model';

export type EntityResponseType = HttpResponse<IChallengeType>;
export type EntityArrayResponseType = HttpResponse<IChallengeType[]>;

@Injectable({ providedIn: 'root' })
export class ChallengeTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/challenge-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(challengeType: IChallengeType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(challengeType);
    return this.http
      .post<IChallengeType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(challengeType: IChallengeType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(challengeType);
    return this.http
      .put<IChallengeType>(`${this.resourceUrl}/${getChallengeTypeIdentifier(challengeType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(challengeType: IChallengeType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(challengeType);
    return this.http
      .patch<IChallengeType>(`${this.resourceUrl}/${getChallengeTypeIdentifier(challengeType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChallengeType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChallengeType[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChallengeTypeToCollectionIfMissing(
    challengeTypeCollection: IChallengeType[],
    ...challengeTypesToCheck: (IChallengeType | null | undefined)[]
  ): IChallengeType[] {
    const challengeTypes: IChallengeType[] = challengeTypesToCheck.filter(isPresent);
    if (challengeTypes.length > 0) {
      const challengeTypeCollectionIdentifiers = challengeTypeCollection.map(
        challengeTypeItem => getChallengeTypeIdentifier(challengeTypeItem)!
      );
      const challengeTypesToAdd = challengeTypes.filter(challengeTypeItem => {
        const challengeTypeIdentifier = getChallengeTypeIdentifier(challengeTypeItem);
        if (challengeTypeIdentifier == null || challengeTypeCollectionIdentifiers.includes(challengeTypeIdentifier)) {
          return false;
        }
        challengeTypeCollectionIdentifiers.push(challengeTypeIdentifier);
        return true;
      });
      return [...challengeTypesToAdd, ...challengeTypeCollection];
    }
    return challengeTypeCollection;
  }

  protected convertDateFromClient(challengeType: IChallengeType): IChallengeType {
    return Object.assign({}, challengeType, {
      inActiveDate: challengeType.inActiveDate?.isValid() ? challengeType.inActiveDate.toJSON() : undefined,
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
      res.body.forEach((challengeType: IChallengeType) => {
        challengeType.inActiveDate = challengeType.inActiveDate ? dayjs(challengeType.inActiveDate) : undefined;
      });
    }
    return res;
  }
}
