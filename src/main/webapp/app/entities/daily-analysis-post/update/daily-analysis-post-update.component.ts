import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDailyAnalysisPost, DailyAnalysisPost } from '../daily-analysis-post.model';
import { DailyAnalysisPostService } from '../service/daily-analysis-post.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IInstrument } from 'app/entities/instrument/instrument.model';
import { InstrumentService } from 'app/entities/instrument/service/instrument.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-daily-analysis-post-update',
  templateUrl: './daily-analysis-post-update.component.html',
})
export class DailyAnalysisPostUpdateComponent implements OnInit {
  isSaving = false;

  instrumentsSharedCollection: IInstrument[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    postTitle: [null, [Validators.required]],
    dateAdded: [null, [Validators.required]],
    backgroundVolume: [],
    overallThoughts: [],
    weeklyChart: [],
    weeklyChartContentType: [],
    dailyChart: [],
    dailyChartContentType: [],
    oneHrChart: [],
    oneHrChartContentType: [],
    planForToday: [],
    makePublicVisibleOnSite: [],
    instrument: [],
    user: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected dailyAnalysisPostService: DailyAnalysisPostService,
    protected instrumentService: InstrumentService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dailyAnalysisPost }) => {
      if (dailyAnalysisPost.id === undefined) {
        const today = dayjs().startOf('day');
        dailyAnalysisPost.dateAdded = today;
      }

      this.updateForm(dailyAnalysisPost);

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
    const dailyAnalysisPost = this.createFromForm();
    if (dailyAnalysisPost.id !== undefined) {
      this.subscribeToSaveResponse(this.dailyAnalysisPostService.update(dailyAnalysisPost));
    } else {
      this.subscribeToSaveResponse(this.dailyAnalysisPostService.create(dailyAnalysisPost));
    }
  }

  trackInstrumentById(index: number, item: IInstrument): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDailyAnalysisPost>>): void {
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

  protected updateForm(dailyAnalysisPost: IDailyAnalysisPost): void {
    this.editForm.patchValue({
      id: dailyAnalysisPost.id,
      postTitle: dailyAnalysisPost.postTitle,
      dateAdded: dailyAnalysisPost.dateAdded ? dailyAnalysisPost.dateAdded.format(DATE_TIME_FORMAT) : null,
      backgroundVolume: dailyAnalysisPost.backgroundVolume,
      overallThoughts: dailyAnalysisPost.overallThoughts,
      weeklyChart: dailyAnalysisPost.weeklyChart,
      weeklyChartContentType: dailyAnalysisPost.weeklyChartContentType,
      dailyChart: dailyAnalysisPost.dailyChart,
      dailyChartContentType: dailyAnalysisPost.dailyChartContentType,
      oneHrChart: dailyAnalysisPost.oneHrChart,
      oneHrChartContentType: dailyAnalysisPost.oneHrChartContentType,
      planForToday: dailyAnalysisPost.planForToday,
      makePublicVisibleOnSite: dailyAnalysisPost.makePublicVisibleOnSite,
      instrument: dailyAnalysisPost.instrument,
      user: dailyAnalysisPost.user,
    });

    this.instrumentsSharedCollection = this.instrumentService.addInstrumentToCollectionIfMissing(
      this.instrumentsSharedCollection,
      dailyAnalysisPost.instrument
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, dailyAnalysisPost.user);
  }

  protected loadRelationshipsOptions(): void {
    this.instrumentService
      .query()
      .pipe(map((res: HttpResponse<IInstrument[]>) => res.body ?? []))
      .pipe(
        map((instruments: IInstrument[]) =>
          this.instrumentService.addInstrumentToCollectionIfMissing(instruments, this.editForm.get('instrument')!.value)
        )
      )
      .subscribe((instruments: IInstrument[]) => (this.instrumentsSharedCollection = instruments));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IDailyAnalysisPost {
    return {
      ...new DailyAnalysisPost(),
      id: this.editForm.get(['id'])!.value,
      postTitle: this.editForm.get(['postTitle'])!.value,
      dateAdded: this.editForm.get(['dateAdded'])!.value ? dayjs(this.editForm.get(['dateAdded'])!.value, DATE_TIME_FORMAT) : undefined,
      backgroundVolume: this.editForm.get(['backgroundVolume'])!.value,
      overallThoughts: this.editForm.get(['overallThoughts'])!.value,
      weeklyChartContentType: this.editForm.get(['weeklyChartContentType'])!.value,
      weeklyChart: this.editForm.get(['weeklyChart'])!.value,
      dailyChartContentType: this.editForm.get(['dailyChartContentType'])!.value,
      dailyChart: this.editForm.get(['dailyChart'])!.value,
      oneHrChartContentType: this.editForm.get(['oneHrChartContentType'])!.value,
      oneHrChart: this.editForm.get(['oneHrChart'])!.value,
      planForToday: this.editForm.get(['planForToday'])!.value,
      makePublicVisibleOnSite: this.editForm.get(['makePublicVisibleOnSite'])!.value,
      instrument: this.editForm.get(['instrument'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
