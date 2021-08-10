import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAddressDetails } from '../address-details.model';
import { AddressDetailsService } from '../service/address-details.service';
import { AddressDetailsDeleteDialogComponent } from '../delete/address-details-delete-dialog.component';

@Component({
  selector: 'jhi-address-details',
  templateUrl: './address-details.component.html',
})
export class AddressDetailsComponent implements OnInit {
  addressDetails?: IAddressDetails[];
  isLoading = false;

  constructor(protected addressDetailsService: AddressDetailsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.addressDetailsService.query().subscribe(
      (res: HttpResponse<IAddressDetails[]>) => {
        this.isLoading = false;
        this.addressDetails = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAddressDetails): number {
    return item.id!;
  }

  delete(addressDetails: IAddressDetails): void {
    const modalRef = this.modalService.open(AddressDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.addressDetails = addressDetails;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
