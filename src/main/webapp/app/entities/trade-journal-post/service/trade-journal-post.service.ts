import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITradeJournalPost, getTradeJournalPostIdentifier } from '../trade-journal-post.model';

export type EntityResponseType = HttpResponse<ITradeJournalPost>;
export type EntityArrayResponseType = HttpResponse<ITradeJournalPost[]>;

@Injectable({ providedIn: 'root' })
export class TradeJournalPostService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trade-journal-posts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tradeJournalPost: ITradeJournalPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeJournalPost);
    return this.http
      .post<ITradeJournalPost>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tradeJournalPost: ITradeJournalPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeJournalPost);
    return this.http
      .put<ITradeJournalPost>(`${this.resourceUrl}/${getTradeJournalPostIdentifier(tradeJournalPost) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tradeJournalPost: ITradeJournalPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tradeJournalPost);
    return this.http
      .patch<ITradeJournalPost>(`${this.resourceUrl}/${getTradeJournalPostIdentifier(tradeJournalPost) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITradeJournalPost>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITradeJournalPost[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTradeJournalPostToCollectionIfMissing(
    tradeJournalPostCollection: ITradeJournalPost[],
    ...tradeJournalPostsToCheck: (ITradeJournalPost | null | undefined)[]
  ): ITradeJournalPost[] {
    const tradeJournalPosts: ITradeJournalPost[] = tradeJournalPostsToCheck.filter(isPresent);
    if (tradeJournalPosts.length > 0) {
      const tradeJournalPostCollectionIdentifiers = tradeJournalPostCollection.map(
        tradeJournalPostItem => getTradeJournalPostIdentifier(tradeJournalPostItem)!
      );
      const tradeJournalPostsToAdd = tradeJournalPosts.filter(tradeJournalPostItem => {
        const tradeJournalPostIdentifier = getTradeJournalPostIdentifier(tradeJournalPostItem);
        if (tradeJournalPostIdentifier == null || tradeJournalPostCollectionIdentifiers.includes(tradeJournalPostIdentifier)) {
          return false;
        }
        tradeJournalPostCollectionIdentifiers.push(tradeJournalPostIdentifier);
        return true;
      });
      return [...tradeJournalPostsToAdd, ...tradeJournalPostCollection];
    }
    return tradeJournalPostCollection;
  }

  protected convertDateFromClient(tradeJournalPost: ITradeJournalPost): ITradeJournalPost {
    return Object.assign({}, tradeJournalPost, {
      dateAdded: tradeJournalPost.dateAdded?.isValid() ? tradeJournalPost.dateAdded.toJSON() : undefined,
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
      res.body.forEach((tradeJournalPost: ITradeJournalPost) => {
        tradeJournalPost.dateAdded = tradeJournalPost.dateAdded ? dayjs(tradeJournalPost.dateAdded) : undefined;
      });
    }
    return res;
  }
}
