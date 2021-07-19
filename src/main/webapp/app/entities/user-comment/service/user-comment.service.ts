import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserComment, getUserCommentIdentifier } from '../user-comment.model';

export type EntityResponseType = HttpResponse<IUserComment>;
export type EntityArrayResponseType = HttpResponse<IUserComment[]>;

@Injectable({ providedIn: 'root' })
export class UserCommentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-comments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userComment: IUserComment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userComment);
    return this.http
      .post<IUserComment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(userComment: IUserComment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userComment);
    return this.http
      .put<IUserComment>(`${this.resourceUrl}/${getUserCommentIdentifier(userComment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(userComment: IUserComment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userComment);
    return this.http
      .patch<IUserComment>(`${this.resourceUrl}/${getUserCommentIdentifier(userComment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserComment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserComment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserCommentToCollectionIfMissing(
    userCommentCollection: IUserComment[],
    ...userCommentsToCheck: (IUserComment | null | undefined)[]
  ): IUserComment[] {
    const userComments: IUserComment[] = userCommentsToCheck.filter(isPresent);
    if (userComments.length > 0) {
      const userCommentCollectionIdentifiers = userCommentCollection.map(userCommentItem => getUserCommentIdentifier(userCommentItem)!);
      const userCommentsToAdd = userComments.filter(userCommentItem => {
        const userCommentIdentifier = getUserCommentIdentifier(userCommentItem);
        if (userCommentIdentifier == null || userCommentCollectionIdentifiers.includes(userCommentIdentifier)) {
          return false;
        }
        userCommentCollectionIdentifiers.push(userCommentIdentifier);
        return true;
      });
      return [...userCommentsToAdd, ...userCommentCollection];
    }
    return userCommentCollection;
  }

  protected convertDateFromClient(userComment: IUserComment): IUserComment {
    return Object.assign({}, userComment, {
      dateAdded: userComment.dateAdded?.isValid() ? userComment.dateAdded.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateAdded = res.body.dateAdded ? dayjs(res.body.dateAdded) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((userComment: IUserComment) => {
        userComment.dateAdded = userComment.dateAdded ? dayjs(userComment.dateAdded) : undefined;
      });
    }
    return res;
  }
}
