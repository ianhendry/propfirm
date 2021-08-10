jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { Mt4AccountService } from '../service/mt-4-account.service';
import { IMt4Account, Mt4Account } from '../mt-4-account.model';

import { Mt4AccountUpdateComponent } from './mt-4-account-update.component';

describe('Component Tests', () => {
  describe('Mt4Account Management Update Component', () => {
    let comp: Mt4AccountUpdateComponent;
    let fixture: ComponentFixture<Mt4AccountUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mt4AccountService: Mt4AccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [Mt4AccountUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(Mt4AccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(Mt4AccountUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mt4AccountService = TestBed.inject(Mt4AccountService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const mt4Account: IMt4Account = { id: 456 };

        activatedRoute.data = of({ mt4Account });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mt4Account));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mt4Account>>();
        const mt4Account = { id: 123 };
        jest.spyOn(mt4AccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mt4Account });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mt4Account }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mt4AccountService.update).toHaveBeenCalledWith(mt4Account);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mt4Account>>();
        const mt4Account = new Mt4Account();
        jest.spyOn(mt4AccountService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mt4Account });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mt4Account }));
        saveSubject.complete();

        // THEN
        expect(mt4AccountService.create).toHaveBeenCalledWith(mt4Account);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mt4Account>>();
        const mt4Account = { id: 123 };
        jest.spyOn(mt4AccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mt4Account });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mt4AccountService.update).toHaveBeenCalledWith(mt4Account);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
