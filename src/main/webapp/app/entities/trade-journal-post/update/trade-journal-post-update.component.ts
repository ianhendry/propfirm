import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITradeJournalPost, TradeJournalPost } from '../trade-journal-post.model';
import { TradeJournalPostService } from '../service/trade-journal-post.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-trade-journal-post-update',
  templateUrl: './trade-journal-post-update.component.html',
})
export class TradeJournalPostUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    postTitle: [null, [Validators.required]],
    dateAdded: [null, [Validators.required]],
    thoughtsOnPsychology: [],
    thoughtsOnTradeProcessAccuracy: [],
    thoughtsOnAreasOfStrength: [],
    thoughtsOnAreasForImprovement: [],
    areaOfFocusForTomorrow: [],
    makePublicVisibleOnSite: [],
    anyMedia: [],
    anyMediaContentType: [],
    anyImage: [],
    anyImageContentType: [],
    user: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tradeJournalPostService: TradeJournalPostService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tradeJournalPost }) => {
      if (tradeJournalPost.id === undefined) {
        const today = dayjs().startOf('day');
        tradeJournalPost.dateAdded = today;
      }

      this.updateForm(tradeJournalPost);

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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tradeJournalPost = this.createFromForm();
    if (tradeJournalPost.id !== undefined) {
      this.subscribeToSaveResponse(this.tradeJournalPostService.update(tradeJournalPost));
    } else {
      this.subscribeToSaveResponse(this.tradeJournalPostService.create(tradeJournalPost));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITradeJournalPost>>): void {
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

  protected updateForm(tradeJournalPost: ITradeJournalPost): void {
    this.editForm.patchValue({
      id: tradeJournalPost.id,
      postTitle: tradeJournalPost.postTitle,
      dateAdded: tradeJournalPost.dateAdded ? tradeJournalPost.dateAdded.format(DATE_TIME_FORMAT) : null,
      thoughtsOnPsychology: tradeJournalPost.thoughtsOnPsychology,
      thoughtsOnTradeProcessAccuracy: tradeJournalPost.thoughtsOnTradeProcessAccuracy,
      thoughtsOnAreasOfStrength: tradeJournalPost.thoughtsOnAreasOfStrength,
      thoughtsOnAreasForImprovement: tradeJournalPost.thoughtsOnAreasForImprovement,
      areaOfFocusForTomorrow: tradeJournalPost.areaOfFocusForTomorrow,
      makePublicVisibleOnSite: tradeJournalPost.makePublicVisibleOnSite,
      anyMedia: tradeJournalPost.anyMedia,
      anyMediaContentType: tradeJournalPost.anyMediaContentType,
      anyImage: tradeJournalPost.anyImage,
      anyImageContentType: tradeJournalPost.anyImageContentType,
      user: tradeJournalPost.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, tradeJournalPost.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ITradeJournalPost {
    return {
      ...new TradeJournalPost(),
      id: this.editForm.get(['id'])!.value,
      postTitle: this.editForm.get(['postTitle'])!.value,
      dateAdded: this.editForm.get(['dateAdded'])!.value ? dayjs(this.editForm.get(['dateAdded'])!.value, DATE_TIME_FORMAT) : undefined,
      thoughtsOnPsychology: this.editForm.get(['thoughtsOnPsychology'])!.value,
      thoughtsOnTradeProcessAccuracy: this.editForm.get(['thoughtsOnTradeProcessAccuracy'])!.value,
      thoughtsOnAreasOfStrength: this.editForm.get(['thoughtsOnAreasOfStrength'])!.value,
      thoughtsOnAreasForImprovement: this.editForm.get(['thoughtsOnAreasForImprovement'])!.value,
      areaOfFocusForTomorrow: this.editForm.get(['areaOfFocusForTomorrow'])!.value,
      makePublicVisibleOnSite: this.editForm.get(['makePublicVisibleOnSite'])!.value,
      anyMediaContentType: this.editForm.get(['anyMediaContentType'])!.value,
      anyMedia: this.editForm.get(['anyMedia'])!.value,
      anyImageContentType: this.editForm.get(['anyImageContentType'])!.value,
      anyImage: this.editForm.get(['anyImage'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
