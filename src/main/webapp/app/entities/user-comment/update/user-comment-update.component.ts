import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUserComment, UserComment } from '../user-comment.model';
import { UserCommentService } from '../service/user-comment.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ITradeJournalPost } from 'app/entities/trade-journal-post/trade-journal-post.model';
import { TradeJournalPostService } from 'app/entities/trade-journal-post/service/trade-journal-post.service';
import { IDailyAnalysisPost } from 'app/entities/daily-analysis-post/daily-analysis-post.model';
import { DailyAnalysisPostService } from 'app/entities/daily-analysis-post/service/daily-analysis-post.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-comment-update',
  templateUrl: './user-comment-update.component.html',
})
export class UserCommentUpdateComponent implements OnInit {
  isSaving = false;

  tradeJournalPostsSharedCollection: ITradeJournalPost[] = [];
  dailyAnalysisPostsSharedCollection: IDailyAnalysisPost[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    commentTitle: [null, [Validators.required]],
    commentBody: [],
    commentMedia: [],
    commentMediaContentType: [],
    dateAdded: [null, [Validators.required]],
    makePublicVisibleOnSite: [],
    tradeJournalPost: [],
    dailyAnalysisPost: [],
    user: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected userCommentService: UserCommentService,
    protected tradeJournalPostService: TradeJournalPostService,
    protected dailyAnalysisPostService: DailyAnalysisPostService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userComment }) => {
      if (userComment.id === undefined) {
        const today = dayjs().startOf('day');
        userComment.dateAdded = today;
      }

      this.updateForm(userComment);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('propfirmApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userComment = this.createFromForm();
    if (userComment.id !== undefined) {
      this.subscribeToSaveResponse(this.userCommentService.update(userComment));
    } else {
      this.subscribeToSaveResponse(this.userCommentService.create(userComment));
    }
  }

  trackTradeJournalPostById(index: number, item: ITradeJournalPost): number {
    return item.id!;
  }

  trackDailyAnalysisPostById(index: number, item: IDailyAnalysisPost): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserComment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userComment: IUserComment): void {
    this.editForm.patchValue({
      id: userComment.id,
      commentTitle: userComment.commentTitle,
      commentBody: userComment.commentBody,
      commentMedia: userComment.commentMedia,
      commentMediaContentType: userComment.commentMediaContentType,
      dateAdded: userComment.dateAdded ? userComment.dateAdded.format(DATE_TIME_FORMAT) : null,
      makePublicVisibleOnSite: userComment.makePublicVisibleOnSite,
      tradeJournalPost: userComment.tradeJournalPost,
      dailyAnalysisPost: userComment.dailyAnalysisPost,
      user: userComment.user,
    });

    this.tradeJournalPostsSharedCollection = this.tradeJournalPostService.addTradeJournalPostToCollectionIfMissing(
      this.tradeJournalPostsSharedCollection,
      userComment.tradeJournalPost
    );
    this.dailyAnalysisPostsSharedCollection = this.dailyAnalysisPostService.addDailyAnalysisPostToCollectionIfMissing(
      this.dailyAnalysisPostsSharedCollection,
      userComment.dailyAnalysisPost
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userComment.user);
  }

  protected loadRelationshipsOptions(): void {
    this.tradeJournalPostService
      .query()
      .pipe(map((res: HttpResponse<ITradeJournalPost[]>) => res.body ?? []))
      .pipe(
        map((tradeJournalPosts: ITradeJournalPost[]) =>
          this.tradeJournalPostService.addTradeJournalPostToCollectionIfMissing(
            tradeJournalPosts,
            this.editForm.get('tradeJournalPost')!.value
          )
        )
      )
      .subscribe((tradeJournalPosts: ITradeJournalPost[]) => (this.tradeJournalPostsSharedCollection = tradeJournalPosts));

    this.dailyAnalysisPostService
      .query()
      .pipe(map((res: HttpResponse<IDailyAnalysisPost[]>) => res.body ?? []))
      .pipe(
        map((dailyAnalysisPosts: IDailyAnalysisPost[]) =>
          this.dailyAnalysisPostService.addDailyAnalysisPostToCollectionIfMissing(
            dailyAnalysisPosts,
            this.editForm.get('dailyAnalysisPost')!.value
          )
        )
      )
      .subscribe((dailyAnalysisPosts: IDailyAnalysisPost[]) => (this.dailyAnalysisPostsSharedCollection = dailyAnalysisPosts));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IUserComment {
    return {
      ...new UserComment(),
      id: this.editForm.get(['id'])!.value,
      commentTitle: this.editForm.get(['commentTitle'])!.value,
      commentBody: this.editForm.get(['commentBody'])!.value,
      commentMediaContentType: this.editForm.get(['commentMediaContentType'])!.value,
      commentMedia: this.editForm.get(['commentMedia'])!.value,
      dateAdded: this.editForm.get(['dateAdded'])!.value ? dayjs(this.editForm.get(['dateAdded'])!.value, DATE_TIME_FORMAT) : undefined,
      makePublicVisibleOnSite: this.editForm.get(['makePublicVisibleOnSite'])!.value,
      tradeJournalPost: this.editForm.get(['tradeJournalPost'])!.value,
      dailyAnalysisPost: this.editForm.get(['dailyAnalysisPost'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
