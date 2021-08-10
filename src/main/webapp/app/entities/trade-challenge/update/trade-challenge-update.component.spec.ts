jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TradeChallengeService } from '../service/trade-challenge.service';
import { ITradeChallenge, TradeChallenge } from '../trade-challenge.model';
import { IMt4Account } from 'app/entities/mt-4-account/mt-4-account.model';
import { Mt4AccountService } from 'app/entities/mt-4-account/service/mt-4-account.service';
import { ISiteAccount } from 'app/entities/site-account/site-account.model';
import { SiteAccountService } from 'app/entities/site-account/service/site-account.service';
import { IChallengeType } from 'app/entities/challenge-type/challenge-type.model';
import { ChallengeTypeService } from 'app/entities/challenge-type/service/challenge-type.service';

import { TradeChallengeUpdateComponent } from './trade-challenge-update.component';

describe('Component Tests', () => {
  describe('TradeChallenge Management Update Component', () => {
    let comp: TradeChallengeUpdateComponent;
    let fixture: ComponentFixture<TradeChallengeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tradeChallengeService: TradeChallengeService;
    let mt4AccountService: Mt4AccountService;
    let siteAccountService: SiteAccountService;
    let challengeTypeService: ChallengeTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TradeChallengeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TradeChallengeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TradeChallengeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tradeChallengeService = TestBed.inject(TradeChallengeService);
      mt4AccountService = TestBed.inject(Mt4AccountService);
      siteAccountService = TestBed.inject(SiteAccountService);
      challengeTypeService = TestBed.inject(ChallengeTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call mt4Account query and add missing value', () => {
        const tradeChallenge: ITradeChallenge = { id: 456 };
        const mt4Account: IMt4Account = { id: 96975 };
        tradeChallenge.mt4Account = mt4Account;

        const mt4AccountCollection: IMt4Account[] = [{ id: 18004 }];
        jest.spyOn(mt4AccountService, 'query').mockReturnValue(of(new HttpResponse({ body: mt4AccountCollection })));
        const expectedCollection: IMt4Account[] = [mt4Account, ...mt4AccountCollection];
        jest.spyOn(mt4AccountService, 'addMt4AccountToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tradeChallenge });
        comp.ngOnInit();

        expect(mt4AccountService.query).toHaveBeenCalled();
        expect(mt4AccountService.addMt4AccountToCollectionIfMissing).toHaveBeenCalledWith(mt4AccountCollection, mt4Account);
        expect(comp.mt4AccountsCollection).toEqual(expectedCollection);
      });

      it('Should call SiteAccount query and add missing value', () => {
        const tradeChallenge: ITradeChallenge = { id: 456 };
        const siteAccount: ISiteAccount = { id: 65465 };
        tradeChallenge.siteAccount = siteAccount;

        const siteAccountCollection: ISiteAccount[] = [{ id: 33298 }];
        jest.spyOn(siteAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: siteAccountCollection })));
        const additionalSiteAccounts = [siteAccount];
        const expectedCollection: ISiteAccount[] = [...additionalSiteAccounts, ...siteAccountCollection];
        jest.spyOn(siteAccountService, 'addSiteAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tradeChallenge });
        comp.ngOnInit();

        expect(siteAccountService.query).toHaveBeenCalled();
        expect(siteAccountService.addSiteAccountToCollectionIfMissing).toHaveBeenCalledWith(
          siteAccountCollection,
          ...additionalSiteAccounts
        );
        expect(comp.siteAccountsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ChallengeType query and add missing value', () => {
        const tradeChallenge: ITradeChallenge = { id: 456 };
        const challengeType: IChallengeType = { id: 42849 };
        tradeChallenge.challengeType = challengeType;

        const challengeTypeCollection: IChallengeType[] = [{ id: 89626 }];
        jest.spyOn(challengeTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: challengeTypeCollection })));
        const additionalChallengeTypes = [challengeType];
        const expectedCollection: IChallengeType[] = [...additionalChallengeTypes, ...challengeTypeCollection];
        jest.spyOn(challengeTypeService, 'addChallengeTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tradeChallenge });
        comp.ngOnInit();

        expect(challengeTypeService.query).toHaveBeenCalled();
        expect(challengeTypeService.addChallengeTypeToCollectionIfMissing).toHaveBeenCalledWith(
          challengeTypeCollection,
          ...additionalChallengeTypes
        );
        expect(comp.challengeTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tradeChallenge: ITradeChallenge = { id: 456 };
        const mt4Account: IMt4Account = { id: 65931 };
        tradeChallenge.mt4Account = mt4Account;
        const siteAccount: ISiteAccount = { id: 90833 };
        tradeChallenge.siteAccount = siteAccount;
        const challengeType: IChallengeType = { id: 83893 };
        tradeChallenge.challengeType = challengeType;

        activatedRoute.data = of({ tradeChallenge });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tradeChallenge));
        expect(comp.mt4AccountsCollection).toContain(mt4Account);
        expect(comp.siteAccountsSharedCollection).toContain(siteAccount);
        expect(comp.challengeTypesSharedCollection).toContain(challengeType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TradeChallenge>>();
        const tradeChallenge = { id: 123 };
        jest.spyOn(tradeChallengeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeChallenge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tradeChallenge }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tradeChallengeService.update).toHaveBeenCalledWith(tradeChallenge);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TradeChallenge>>();
        const tradeChallenge = new TradeChallenge();
        jest.spyOn(tradeChallengeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeChallenge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tradeChallenge }));
        saveSubject.complete();

        // THEN
        expect(tradeChallengeService.create).toHaveBeenCalledWith(tradeChallenge);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TradeChallenge>>();
        const tradeChallenge = { id: 123 };
        jest.spyOn(tradeChallengeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeChallenge });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tradeChallengeService.update).toHaveBeenCalledWith(tradeChallenge);
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

      describe('trackSiteAccountById', () => {
        it('Should return tracked SiteAccount primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSiteAccountById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackChallengeTypeById', () => {
        it('Should return tracked ChallengeType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackChallengeTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
