import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISiteAccount, SiteAccount } from '../site-account.model';
import { SiteAccountService } from '../service/site-account.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IAddressDetails } from 'app/entities/address-details/address-details.model';
import { AddressDetailsService } from 'app/entities/address-details/service/address-details.service';

@Component({
  selector: 'jhi-site-account-update',
  templateUrl: './site-account-update.component.html',
})
export class SiteAccountUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  addressDetailsSharedCollection: IAddressDetails[] = [];

  editForm = this.fb.group({
    id: [],
    accountName: [],
    userPicture: [],
    userPictureContentType: [],
    userBio: [],
    inActive: [],
    inActiveDate: [],
    user: [],
    addressDetails: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected siteAccountService: SiteAccountService,
    protected userService: UserService,
    protected addressDetailsService: AddressDetailsService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ siteAccount }) => {
      if (siteAccount.id === undefined) {
        const today = dayjs().startOf('day');
        siteAccount.inActiveDate = today;
      }

      this.updateForm(siteAccount);

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
    const siteAccount = this.createFromForm();
    if (siteAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.siteAccountService.update(siteAccount));
    } else {
      this.subscribeToSaveResponse(this.siteAccountService.create(siteAccount));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackAddressDetailsById(index: number, item: IAddressDetails): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISiteAccount>>): void {
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

  protected updateForm(siteAccount: ISiteAccount): void {
    this.editForm.patchValue({
      id: siteAccount.id,
      accountName: siteAccount.accountName,
      userPicture: siteAccount.userPicture,
      userPictureContentType: siteAccount.userPictureContentType,
      userBio: siteAccount.userBio,
      inActive: siteAccount.inActive,
      inActiveDate: siteAccount.inActiveDate ? siteAccount.inActiveDate.format(DATE_TIME_FORMAT) : null,
      user: siteAccount.user,
      addressDetails: siteAccount.addressDetails,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, siteAccount.user);
    this.addressDetailsSharedCollection = this.addressDetailsService.addAddressDetailsToCollectionIfMissing(
      this.addressDetailsSharedCollection,
      siteAccount.addressDetails
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.addressDetailsService
      .query()
      .pipe(map((res: HttpResponse<IAddressDetails[]>) => res.body ?? []))
      .pipe(
        map((addressDetails: IAddressDetails[]) =>
          this.addressDetailsService.addAddressDetailsToCollectionIfMissing(addressDetails, this.editForm.get('addressDetails')!.value)
        )
      )
      .subscribe((addressDetails: IAddressDetails[]) => (this.addressDetailsSharedCollection = addressDetails));
  }

  protected createFromForm(): ISiteAccount {
    return {
      ...new SiteAccount(),
      id: this.editForm.get(['id'])!.value,
      accountName: this.editForm.get(['accountName'])!.value,
      userPictureContentType: this.editForm.get(['userPictureContentType'])!.value,
      userPicture: this.editForm.get(['userPicture'])!.value,
      userBio: this.editForm.get(['userBio'])!.value,
      inActive: this.editForm.get(['inActive'])!.value,
      inActiveDate: this.editForm.get(['inActiveDate'])!.value
        ? dayjs(this.editForm.get(['inActiveDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
      addressDetails: this.editForm.get(['addressDetails'])!.value,
    };
  }
}
