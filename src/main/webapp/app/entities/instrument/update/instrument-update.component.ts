import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInstrument, Instrument } from '../instrument.model';
import { InstrumentService } from '../service/instrument.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-instrument-update',
  templateUrl: './instrument-update.component.html',
})
export class InstrumentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ticker: [],
    instrumentType: [],
    exchange: [],
    averageSpread: [],
    tradeRestrictions: [],
    inActive: [],
    inActiveDate: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected instrumentService: InstrumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ instrument }) => {
      if (instrument.id === undefined) {
        const today = dayjs().startOf('day');
        instrument.inActiveDate = today;
      }

      this.updateForm(instrument);
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
    const instrument = this.createFromForm();
    if (instrument.id !== undefined) {
      this.subscribeToSaveResponse(this.instrumentService.update(instrument));
    } else {
      this.subscribeToSaveResponse(this.instrumentService.create(instrument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstrument>>): void {
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

  protected updateForm(instrument: IInstrument): void {
    this.editForm.patchValue({
      id: instrument.id,
      ticker: instrument.ticker,
      instrumentType: instrument.instrumentType,
      exchange: instrument.exchange,
      averageSpread: instrument.averageSpread,
      tradeRestrictions: instrument.tradeRestrictions,
      inActive: instrument.inActive,
      inActiveDate: instrument.inActiveDate ? instrument.inActiveDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IInstrument {
    return {
      ...new Instrument(),
      id: this.editForm.get(['id'])!.value,
      ticker: this.editForm.get(['ticker'])!.value,
      instrumentType: this.editForm.get(['instrumentType'])!.value,
      exchange: this.editForm.get(['exchange'])!.value,
      averageSpread: this.editForm.get(['averageSpread'])!.value,
      tradeRestrictions: this.editForm.get(['tradeRestrictions'])!.value,
      inActive: this.editForm.get(['inActive'])!.value,
      inActiveDate: this.editForm.get(['inActiveDate'])!.value
        ? dayjs(this.editForm.get(['inActiveDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
