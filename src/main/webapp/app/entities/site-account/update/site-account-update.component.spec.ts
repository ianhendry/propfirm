jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SiteAccountService } from '../service/site-account.service';
import { ISiteAccount, SiteAccount } from '../site-account.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IAddressDetails } from 'app/entities/address-details/address-details.model';
import { AddressDetailsService } from 'app/entities/address-details/service/address-details.service';

import { SiteAccountUpdateComponent } from './site-account-update.component';

describe('Component Tests', () => {
  describe('SiteAccount Management Update Component', () => {
    let comp: SiteAccountUpdateComponent;
    let fixture: ComponentFixture<SiteAccountUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let siteAccountService: SiteAccountService;
    let userService: UserService;
    let addressDetailsService: AddressDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SiteAccountUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SiteAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SiteAccountUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      siteAccountService = TestBed.inject(SiteAccountService);
      userService = TestBed.inject(UserService);
      addressDetailsService = TestBed.inject(AddressDetailsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const siteAccount: ISiteAccount = { id: 456 };
        const user: IUser = { id: 95529 };
        siteAccount.user = user;

        const userCollection: IUser[] = [{ id: 4807 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ siteAccount });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AddressDetails query and add missing value', () => {
        const siteAccount: ISiteAccount = { id: 456 };
        const addressDetails: IAddressDetails = { id: 58450 };
        siteAccount.addressDetails = addressDetails;

        const addressDetailsCollection: IAddressDetails[] = [{ id: 30812 }];
        jest.spyOn(addressDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: addressDetailsCollection })));
        const additionalAddressDetails = [addressDetails];
        const expectedCollection: IAddressDetails[] = [...additionalAddressDetails, ...addressDetailsCollection];
        jest.spyOn(addressDetailsService, 'addAddressDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ siteAccount });
        comp.ngOnInit();

        expect(addressDetailsService.query).toHaveBeenCalled();
        expect(addressDetailsService.addAddressDetailsToCollectionIfMissing).toHaveBeenCalledWith(
          addressDetailsCollection,
          ...additionalAddressDetails
        );
        expect(comp.addressDetailsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const siteAccount: ISiteAccount = { id: 456 };
        const user: IUser = { id: 63353 };
        siteAccount.user = user;
        const addressDetails: IAddressDetails = { id: 82212 };
        siteAccount.addressDetails = addressDetails;

        activatedRoute.data = of({ siteAccount });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(siteAccount));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.addressDetailsSharedCollection).toContain(addressDetails);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SiteAccount>>();
        const siteAccount = { id: 123 };
        jest.spyOn(siteAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ siteAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: siteAccount }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(siteAccountService.update).toHaveBeenCalledWith(siteAccount);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SiteAccount>>();
        const siteAccount = new SiteAccount();
        jest.spyOn(siteAccountService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ siteAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: siteAccount }));
        saveSubject.complete();

        // THEN
        expect(siteAccountService.create).toHaveBeenCalledWith(siteAccount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SiteAccount>>();
        const siteAccount = { id: 123 };
        jest.spyOn(siteAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ siteAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(siteAccountService.update).toHaveBeenCalledWith(siteAccount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAddressDetailsById', () => {
        it('Should return tracked AddressDetails primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAddressDetailsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
