import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAddressDetails, AddressDetails } from '../address-details.model';
import { AddressDetailsService } from '../service/address-details.service';

@Component({
  selector: 'jhi-address-details-update',
  templateUrl: './address-details-update.component.html',
})
export class AddressDetailsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    contactName: [],
    address1: [],
    address2: [],
    address3: [],
    address4: [],
    address5: [],
    address6: [],
    dialCode: [],
    phoneNumber: [],
    messengerId: [],
    dateAdded: [],
    inActive: [],
    inActiveDate: [],
  });

  constructor(
    protected addressDetailsService: AddressDetailsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ addressDetails }) => {
      if (addressDetails.id === undefined) {
        const today = dayjs().startOf('day');
        addressDetails.dateAdded = today;
        addressDetails.inActiveDate = today;
      }

      this.updateForm(addressDetails);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const addressDetails = this.createFromForm();
    if (addressDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.addressDetailsService.update(addressDetails));
    } else {
      this.subscribeToSaveResponse(this.addressDetailsService.create(addressDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddressDetails>>): void {
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

  protected updateForm(addressDetails: IAddressDetails): void {
    this.editForm.patchValue({
      id: addressDetails.id,
      contactName: addressDetails.contactName,
      address1: addressDetails.address1,
      address2: addressDetails.address2,
      address3: addressDetails.address3,
      address4: addressDetails.address4,
      address5: addressDetails.address5,
      address6: addressDetails.address6,
      dialCode: addressDetails.dialCode,
      phoneNumber: addressDetails.phoneNumber,
      messengerId: addressDetails.messengerId,
      dateAdded: addressDetails.dateAdded ? addressDetails.dateAdded.format(DATE_TIME_FORMAT) : null,
      inActive: addressDetails.inActive,
      inActiveDate: addressDetails.inActiveDate ? addressDetails.inActiveDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IAddressDetails {
    return {
      ...new AddressDetails(),
      id: this.editForm.get(['id'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      address1: this.editForm.get(['address1'])!.value,
      address2: this.editForm.get(['address2'])!.value,
      address3: this.editForm.get(['address3'])!.value,
      address4: this.editForm.get(['address4'])!.value,
      address5: this.editForm.get(['address5'])!.value,
      address6: this.editForm.get(['address6'])!.value,
      dialCode: this.editForm.get(['dialCode'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      messengerId: this.editForm.get(['messengerId'])!.value,
      dateAdded: this.editForm.get(['dateAdded'])!.value ? dayjs(this.editForm.get(['dateAdded'])!.value, DATE_TIME_FORMAT) : undefined,
      inActive: this.editForm.get(['inActive'])!.value,
      inActiveDate: this.editForm.get(['inActiveDate'])!.value
        ? dayjs(this.editForm.get(['inActiveDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
