jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { Mt4TradeService } from '../service/mt-4-trade.service';
import { IMt4Trade, Mt4Trade } from '../mt-4-trade.model';
import { ITradeJournalPost } from 'app/entities/trade-journal-post/trade-journal-post.model';
import { TradeJournalPostService } from 'app/entities/trade-journal-post/service/trade-journal-post.service';
import { IMt4Account } from 'app/entities/mt-4-account/mt-4-account.model';
import { Mt4AccountService } from 'app/entities/mt-4-account/service/mt-4-account.service';
import { IInstrument } from 'app/entities/instrument/instrument.model';
import { InstrumentService } from 'app/entities/instrument/service/instrument.service';

import { Mt4TradeUpdateComponent } from './mt-4-trade-update.component';

describe('Component Tests', () => {
  describe('Mt4Trade Management Update Component', () => {
    let comp: Mt4TradeUpdateComponent;
    let fixture: ComponentFixture<Mt4TradeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mt4TradeService: Mt4TradeService;
    let tradeJournalPostService: TradeJournalPostService;
    let mt4AccountService: Mt4AccountService;
    let instrumentService: InstrumentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [Mt4TradeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(Mt4TradeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(Mt4TradeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mt4TradeService = TestBed.inject(Mt4TradeService);
      tradeJournalPostService = TestBed.inject(TradeJournalPostService);
      mt4AccountService = TestBed.inject(Mt4AccountService);
      instrumentService = TestBed.inject(InstrumentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call tradeJournalPost query and add missing value', () => {
        const mt4Trade: IMt4Trade = { id: 456 };
        const tradeJournalPost: ITradeJournalPost = { id: 42540 };
        mt4Trade.tradeJournalPost = tradeJournalPost;

        const tradeJournalPostCollection: ITradeJournalPost[] = [{ id: 64355 }];
        jest.spyOn(tradeJournalPostService, 'query').mockReturnValue(of(new HttpResponse({ body: tradeJournalPostCollection })));
        const expectedCollection: ITradeJournalPost[] = [tradeJournalPost, ...tradeJournalPostCollection];
        jest.spyOn(tradeJournalPostService, 'addTradeJournalPostToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ mt4Trade });
        comp.ngOnInit();

        expect(tradeJournalPostService.query).toHaveBeenCalled();
        expect(tradeJournalPostService.addTradeJournalPostToCollectionIfMissing).toHaveBeenCalledWith(
          tradeJournalPostCollection,
          tradeJournalPost
        );
        expect(comp.tradeJournalPostsCollection).toEqual(expectedCollection);
      });

      it('Should call Mt4Account query and add missing value', () => {
        const mt4Trade: IMt4Trade = { id: 456 };
        const mt4Account: IMt4Account = { id: 68098 };
        mt4Trade.mt4Account = mt4Account;

        const mt4AccountCollection: IMt4Account[] = [{ id: 92203 }];
        jest.spyOn(mt4AccountService, 'query').mockReturnValue(of(new HttpResponse({ body: mt4AccountCollection })));
        const additionalMt4Accounts = [mt4Account];
        const expectedCollection: IMt4Account[] = [...additionalMt4Accounts, ...mt4AccountCollection];
        jest.spyOn(mt4AccountService, 'addMt4AccountToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ mt4Trade });
        comp.ngOnInit();

        expect(mt4AccountService.query).toHaveBeenCalled();
        expect(mt4AccountService.addMt4AccountToCollectionIfMissing).toHaveBeenCalledWith(mt4AccountCollection, ...additionalMt4Accounts);
        expect(comp.mt4AccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Instrument query and add missing value', () => {
        const mt4Trade: IMt4Trade = { id: 456 };
        const instrument: IInstrument = { id: 66449 };
        mt4Trade.instrument = instrument;

        const instrumentCollection: IInstrument[] = [{ id: 68584 }];
        jest.spyOn(instrumentService, 'query').mockReturnValue(of(new HttpResponse({ body: instrumentCollection })));
        const additionalInstruments = [instrument];
        const expectedCollection: IInstrument[] = [...additionalInstruments, ...instrumentCollection];
        jest.spyOn(instrumentService, 'addInstrumentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ mt4Trade });
        comp.ngOnInit();

        expect(instrumentService.query).toHaveBeenCalled();
        expect(instrumentService.addInstrumentToCollectionIfMissing).toHaveBeenCalledWith(instrumentCollection, ...additionalInstruments);
        expect(comp.instrumentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const mt4Trade: IMt4Trade = { id: 456 };
        const tradeJournalPost: ITradeJournalPost = { id: 77677 };
        mt4Trade.tradeJournalPost = tradeJournalPost;
        const mt4Account: IMt4Account = { id: 42543 };
        mt4Trade.mt4Account = mt4Account;
        const instrument: IInstrument = { id: 60150 };
        mt4Trade.instrument = instrument;

        activatedRoute.data = of({ mt4Trade });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mt4Trade));
        expect(comp.tradeJournalPostsCollection).toContain(tradeJournalPost);
        expect(comp.mt4AccountsSharedCollection).toContain(mt4Account);
        expect(comp.instrumentsSharedCollection).toContain(instrument);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mt4Trade>>();
        const mt4Trade = { id: 123 };
        jest.spyOn(mt4TradeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mt4Trade });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mt4Trade }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mt4TradeService.update).toHaveBeenCalledWith(mt4Trade);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mt4Trade>>();
        const mt4Trade = new Mt4Trade();
        jest.spyOn(mt4TradeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mt4Trade });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mt4Trade }));
        saveSubject.complete();

        // THEN
        expect(mt4TradeService.create).toHaveBeenCalledWith(mt4Trade);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Mt4Trade>>();
        const mt4Trade = { id: 123 };
        jest.spyOn(mt4TradeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mt4Trade });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mt4TradeService.update).toHaveBeenCalledWith(mt4Trade);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTradeJournalPostById', () => {
        it('Should return tracked TradeJournalPost primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTradeJournalPostById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMt4AccountById', () => {
        it('Should return tracked Mt4Account primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMt4AccountById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackInstrumentById', () => {
        it('Should return tracked Instrument primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInstrumentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
