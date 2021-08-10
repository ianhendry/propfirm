import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IChallengeType, ChallengeType } from '../challenge-type.model';
import { ChallengeTypeService } from '../service/challenge-type.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-challenge-type-update',
  templateUrl: './challenge-type-update.component.html',
})
export class ChallengeTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    challengeTypeName: [],
    price: [],
    priceContentType: [],
    refundAfterComplete: [],
    accountSize: [],
    profitTargetPhaseOne: [],
    profitTargetPhaseTwo: [],
    durationDaysPhaseOne: [],
    durationDaysPhaseTwo: [],
    maxDailyDrawdown: [],
    maxTotalDrawDown: [],
    profitSplitRatio: [],
    freeRetry: [],
    userBio: [],
    inActive: [],
    inActiveDate: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected challengeTypeService: ChallengeTypeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ challengeType }) => {
      if (challengeType.id === undefined) {
        const today = dayjs().startOf('day');
        challengeType.inActiveDate = today;
      }

      this.updateForm(challengeType);
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
    const challengeType = this.createFromForm();
    if (challengeType.id !== undefined) {
      this.subscribeToSaveResponse(this.challengeTypeService.update(challengeType));
    } else {
      this.subscribeToSaveResponse(this.challengeTypeService.create(challengeType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChallengeType>>): void {
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

  protected updateForm(challengeType: IChallengeType): void {
    this.editForm.patchValue({
      id: challengeType.id,
      challengeTypeName: challengeType.challengeTypeName,
      price: challengeType.price,
      priceContentType: challengeType.priceContentType,
      refundAfterComplete: challengeType.refundAfterComplete,
      accountSize: challengeType.accountSize,
      profitTargetPhaseOne: challengeType.profitTargetPhaseOne,
      profitTargetPhaseTwo: challengeType.profitTargetPhaseTwo,
      durationDaysPhaseOne: challengeType.durationDaysPhaseOne,
      durationDaysPhaseTwo: challengeType.durationDaysPhaseTwo,
      maxDailyDrawdown: challengeType.maxDailyDrawdown,
      maxTotalDrawDown: challengeType.maxTotalDrawDown,
      profitSplitRatio: challengeType.profitSplitRatio,
      freeRetry: challengeType.freeRetry,
      userBio: challengeType.userBio,
      inActive: challengeType.inActive,
      inActiveDate: challengeType.inActiveDate ? challengeType.inActiveDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IChallengeType {
    return {
      ...new ChallengeType(),
      id: this.editForm.get(['id'])!.value,
      challengeTypeName: this.editForm.get(['challengeTypeName'])!.value,
      priceContentType: this.editForm.get(['priceContentType'])!.value,
      price: this.editForm.get(['price'])!.value,
      refundAfterComplete: this.editForm.get(['refundAfterComplete'])!.value,
      accountSize: this.editForm.get(['accountSize'])!.value,
      profitTargetPhaseOne: this.editForm.get(['profitTargetPhaseOne'])!.value,
      profitTargetPhaseTwo: this.editForm.get(['profitTargetPhaseTwo'])!.value,
      durationDaysPhaseOne: this.editForm.get(['durationDaysPhaseOne'])!.value,
      durationDaysPhaseTwo: this.editForm.get(['durationDaysPhaseTwo'])!.value,
      maxDailyDrawdown: this.editForm.get(['maxDailyDrawdown'])!.value,
      maxTotalDrawDown: this.editForm.get(['maxTotalDrawDown'])!.value,
      profitSplitRatio: this.editForm.get(['profitSplitRatio'])!.value,
      freeRetry: this.editForm.get(['freeRetry'])!.value,
      userBio: this.editForm.get(['userBio'])!.value,
      inActive: this.editForm.get(['inActive'])!.value,
      inActiveDate: this.editForm.get(['inActiveDate'])!.value
        ? dayjs(this.editForm.get(['inActiveDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
