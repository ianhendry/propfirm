import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDailyAnalysisPost, getDailyAnalysisPostIdentifier } from '../daily-analysis-post.model';

export type EntityResponseType = HttpResponse<IDailyAnalysisPost>;
export type EntityArrayResponseType = HttpResponse<IDailyAnalysisPost[]>;

@Injectable({ providedIn: 'root' })
export class DailyAnalysisPostService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/daily-analysis-posts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dailyAnalysisPost: IDailyAnalysisPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyAnalysisPost);
    return this.http
      .post<IDailyAnalysisPost>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dailyAnalysisPost: IDailyAnalysisPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyAnalysisPost);
    return this.http
      .put<IDailyAnalysisPost>(`${this.resourceUrl}/${getDailyAnalysisPostIdentifier(dailyAnalysisPost) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dailyAnalysisPost: IDailyAnalysisPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyAnalysisPost);
    return this.http
      .patch<IDailyAnalysisPost>(`${this.resourceUrl}/${getDailyAnalysisPostIdentifier(dailyAnalysisPost) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDailyAnalysisPost>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDailyAnalysisPost[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDailyAnalysisPostToCollectionIfMissing(
    dailyAnalysisPostCollection: IDailyAnalysisPost[],
    ...dailyAnalysisPostsToCheck: (IDailyAnalysisPost | null | undefined)[]
  ): IDailyAnalysisPost[] {
    const dailyAnalysisPosts: IDailyAnalysisPost[] = dailyAnalysisPostsToCheck.filter(isPresent);
    if (dailyAnalysisPosts.length > 0) {
      const dailyAnalysisPostCollectionIdentifiers = dailyAnalysisPostCollection.map(
        dailyAnalysisPostItem => getDailyAnalysisPostIdentifier(dailyAnalysisPostItem)!
      );
      const dailyAnalysisPostsToAdd = dailyAnalysisPosts.filter(dailyAnalysisPostItem => {
        const dailyAnalysisPostIdentifier = getDailyAnalysisPostIdentifier(dailyAnalysisPostItem);
        if (dailyAnalysisPostIdentifier == null || dailyAnalysisPostCollectionIdentifiers.includes(dailyAnalysisPostIdentifier)) {
          return false;
        }
        dailyAnalysisPostCollectionIdentifiers.push(dailyAnalysisPostIdentifier);
        return true;
      });
      return [...dailyAnalysisPostsToAdd, ...dailyAnalysisPostCollection];
    }
    return dailyAnalysisPostCollection;
  }

  protected convertDateFromClient(dailyAnalysisPost: IDailyAnalysisPost): IDailyAnalysisPost {
    return Object.assign({}, dailyAnalysisPost, {
      dateAdded: dailyAnalysisPost.dateAdded?.isValid() ? dailyAnalysisPost.dateAdded.toJSON() : undefined,
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
      res.body.forEach((dailyAnalysisPost: IDailyAnalysisPost) => {
        dailyAnalysisPost.dateAdded = dailyAnalysisPost.dateAdded ? dayjs(dailyAnalysisPost.dateAdded) : undefined;
      });
    }
    return res;
  }
}
