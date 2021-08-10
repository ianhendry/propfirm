jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AddressDetailsService } from '../service/address-details.service';
import { IAddressDetails, AddressDetails } from '../address-details.model';

import { AddressDetailsUpdateComponent } from './address-details-update.component';

describe('Component Tests', () => {
  describe('AddressDetails Management Update Component', () => {
    let comp: AddressDetailsUpdateComponent;
    let fixture: ComponentFixture<AddressDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let addressDetailsService: AddressDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AddressDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AddressDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AddressDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      addressDetailsService = TestBed.inject(AddressDetailsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const addressDetails: IAddressDetails = { id: 456 };

        activatedRoute.data = of({ addressDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(addressDetails));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AddressDetails>>();
        const addressDetails = { id: 123 };
        jest.spyOn(addressDetailsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ addressDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: addressDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(addressDetailsService.update).toHaveBeenCalledWith(addressDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AddressDetails>>();
        const addressDetails = new AddressDetails();
        jest.spyOn(addressDetailsService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ addressDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: addressDetails }));
        saveSubject.complete();

        // THEN
        expect(addressDetailsService.create).toHaveBeenCalledWith(addressDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AddressDetails>>();
        const addressDetails = { id: 123 };
        jest.spyOn(addressDetailsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ addressDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(addressDetailsService.update).toHaveBeenCalledWith(addressDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
