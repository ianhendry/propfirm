import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInstrument } from '../instrument.model';
import { InstrumentService } from '../service/instrument.service';
import { InstrumentDeleteDialogComponent } from '../delete/instrument-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-instrument',
  templateUrl: './instrument.component.html',
})
export class InstrumentComponent implements OnInit {
  instruments?: IInstrument[];
  isLoading = false;

  constructor(protected instrumentService: InstrumentService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.instrumentService.query().subscribe(
      (res: HttpResponse<IInstrument[]>) => {
        this.isLoading = false;
        this.instruments = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IInstrument): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(instrument: IInstrument): void {
    const modalRef = this.modalService.open(InstrumentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.instrument = instrument;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
