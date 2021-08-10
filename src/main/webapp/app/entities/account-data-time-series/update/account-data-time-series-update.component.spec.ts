jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AccountDataTimeSeriesService } from '../service/account-data-time-series.service';
import { IAccountDataTimeSeries, AccountDataTimeSeries } from '../account-data-time-series.model';
import { IMt4Account } from 'app/entities/mt-4-account/mt-4-account.model';
import { Mt4AccountService } from 'app/entities/mt-4-account/service/mt-4-account.service';

import { AccountDataTimeSeriesUpdateComponent } from './account-data-time-series-update.component';

describe('Component Tests', () => {
  describe('AccountDataTimeSeries Management Update Component', () => {
    let comp: AccountDataTimeSeriesUpdateComponent;
    let fixture: ComponentFixture<AccountDataTimeSeriesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let accountDataTimeSeriesService: AccountDataTimeSeriesService;
    let mt4AccountService: Mt4AccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AccountDataTimeSeriesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AccountDataTimeSeriesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AccountDataTimeSeriesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      accountDataTimeSeriesService = TestBed.inject(AccountDataTimeSeriesService);
      mt4AccountService = TestBed.inject(Mt4AccountService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Mt4Account query and add missing value', () => {
        const accountDataTimeSeries: IAccountDataTimeSeries = { id: 456 };
        const mt4Account: IMt4Account = { id: 58633 };
        accountDataTimeSeries.mt4Account = mt4Account;

        const mt4AccountCollection: IMt4Account[] = [{ id: 7637 }];
        jest.spyOn(mt4AccountService, 'query').mockReturnValue(of(new HttpResponse({ body: mt4AccountCollection })));
        const additionalMt4Accounts = [mt4Account];
        const expectedCollection: IMt4Account[] = [...additionalMt4Accounts, ...mt4AccountCollection];
        jest.spyOn(mt4AccountService, 'addMt4AccountToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ accountDataTimeSeries });
        comp.ngOnInit();

        expect(mt4AccountService.query).toHaveBeenCalled();
        expect(mt4AccountService.addMt4AccountToCollectionIfMissing).toHaveBeenCalledWith(mt4AccountCollection, ...additionalMt4Accounts);
        expect(comp.mt4AccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const accountDataTimeSeries: IAccountDataTimeSeries = { id: 456 };
        const mt4Account: IMt4Account = { id: 52914 };
        accountDataTimeSeries.mt4Account = mt4Account;

        activatedRoute.data = of({ accountDataTimeSeries });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(accountDataTimeSeries));
        expect(comp.mt4AccountsSharedCollection).toContain(mt4Account);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AccountDataTimeSeries>>();
        const accountDataTimeSeries = { id: 123 };
        jest.spyOn(accountDataTimeSeriesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ accountDataTimeSeries });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: accountDataTimeSeries }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(accountDataTimeSeriesService.update).toHaveBeenCalledWith(accountDataTimeSeries);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AccountDataTimeSeries>>();
        const accountDataTimeSeries = new AccountDataTimeSeries();
        jest.spyOn(accountDataTimeSeriesService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ accountDataTimeSeries });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: accountDataTimeSeries }));
        saveSubject.complete();

        // THEN
        expect(accountDataTimeSeriesService.create).toHaveBeenCalledWith(accountDataTimeSeries);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AccountDataTimeSeries>>();
        const accountDataTimeSeries = { id: 123 };
        jest.spyOn(accountDataTimeSeriesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ accountDataTimeSeries });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(accountDataTimeSeriesService.update).toHaveBeenCalledWith(accountDataTimeSeries);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMt4AccountById', () => {
        it('Should return tracked Mt4Account primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMt4AccountById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
