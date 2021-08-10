import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAddressDetails } from '../address-details.model';

@Component({
  selector: 'jhi-address-details-detail',
  templateUrl: './address-details-detail.component.html',
})
export class AddressDetailsDetailComponent implements OnInit {
  addressDetails: IAddressDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ addressDetails }) => {
      this.addressDetails = addressDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
