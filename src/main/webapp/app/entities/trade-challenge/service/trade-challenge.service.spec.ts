import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITradeChallenge, TradeChallenge } from '../trade-challenge.model';

import { TradeChallengeService } from './trade-challenge.service';

describe('Service Tests', () => {
  describe('TradeChallenge Service', () => {
    let service: TradeChallengeService;
    let httpMock: HttpTestingController;
    let elemDefault: ITradeChallenge;
    let expectedResult: ITradeChallenge | ITradeChallenge[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TradeChallengeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        tradeChallengeName: 'AAAAAAA',
        startDate: currentDate,
        runningMaxTotalDrawdown: 0,
        runningMaxDailyDrawdown: 0,
        rulesViolated: false,
        ruleViolated: 'AAAAAAA',
        ruleViolatedDate: currentDate,
        maxTotalDrawdown: 0,
        maxDailyDrawdown: 0,
        lastDailyResetDate: currentDate,
        endDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_TIME_FORMAT),
            ruleViolatedDate: currentDate.format(DATE_TIME_FORMAT),
            lastDailyResetDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TradeChallenge', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_TIME_FORMAT),
            ruleViolatedDate: currentDate.format(DATE_TIME_FORMAT),
            lastDailyResetDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            ruleViolatedDate: currentDate,
            lastDailyResetDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.create(new TradeChallenge()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TradeChallenge', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tradeChallengeName: 'BBBBBB',
            startDate: currentDate.format(DATE_TIME_FORMAT),
            runningMaxTotalDrawdown: 1,
            runningMaxDailyDrawdown: 1,
            rulesViolated: true,
            ruleViolated: 'BBBBBB',
            ruleViolatedDate: currentDate.format(DATE_TIME_FORMAT),
            maxTotalDrawdown: 1,
            maxDailyDrawdown: 1,
            lastDailyResetDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            ruleViolatedDate: currentDate,
            lastDailyResetDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TradeChallenge', () => {
        const patchObject = Object.assign(
          {
            startDate: currentDate.format(DATE_TIME_FORMAT),
            runningMaxTotalDrawdown: 1,
            rulesViolated: true,
            ruleViolatedDate: currentDate.format(DATE_TIME_FORMAT),
            maxTotalDrawdown: 1,
            maxDailyDrawdown: 1,
          },
          new TradeChallenge()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
            ruleViolatedDate: currentDate,
            lastDailyResetDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TradeChallenge', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tradeChallengeName: 'BBBBBB',
            startDate: currentDate.format(DATE_TIME_FORMAT),
            runningMaxTotalDrawdown: 1,
            runningMaxDailyDrawdown: 1,
            rulesViolated: true,
            ruleViolated: 'BBBBBB',
            ruleViolatedDate: currentDate.format(DATE_TIME_FORMAT),
            maxTotalDrawdown: 1,
            maxDailyDrawdown: 1,
            lastDailyResetDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            ruleViolatedDate: currentDate,
            lastDailyResetDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TradeChallenge', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTradeChallengeToCollectionIfMissing', () => {
        it('should add a TradeChallenge to an empty array', () => {
          const tradeChallenge: ITradeChallenge = { id: 123 };
          expectedResult = service.addTradeChallengeToCollectionIfMissing([], tradeChallenge);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tradeChallenge);
        });

        it('should not add a TradeChallenge to an array that contains it', () => {
          const tradeChallenge: ITradeChallenge = { id: 123 };
          const tradeChallengeCollection: ITradeChallenge[] = [
            {
              ...tradeChallenge,
            },
            { id: 456 },
          ];
          expectedResult = service.addTradeChallengeToCollectionIfMissing(tradeChallengeCollection, tradeChallenge);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TradeChallenge to an array that doesn't contain it", () => {
          const tradeChallenge: ITradeChallenge = { id: 123 };
          const tradeChallengeCollection: ITradeChallenge[] = [{ id: 456 }];
          expectedResult = service.addTradeChallengeToCollectionIfMissing(tradeChallengeCollection, tradeChallenge);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tradeChallenge);
        });

        it('should add only unique TradeChallenge to an array', () => {
          const tradeChallengeArray: ITradeChallenge[] = [{ id: 123 }, { id: 456 }, { id: 13000 }];
          const tradeChallengeCollection: ITradeChallenge[] = [{ id: 123 }];
          expectedResult = service.addTradeChallengeToCollectionIfMissing(tradeChallengeCollection, ...tradeChallengeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tradeChallenge: ITradeChallenge = { id: 123 };
          const tradeChallenge2: ITradeChallenge = { id: 456 };
          expectedResult = service.addTradeChallengeToCollectionIfMissing([], tradeChallenge, tradeChallenge2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tradeChallenge);
          expect(expectedResult).toContain(tradeChallenge2);
        });

        it('should accept null and undefined values', () => {
          const tradeChallenge: ITradeChallenge = { id: 123 };
          expectedResult = service.addTradeChallengeToCollectionIfMissing([], null, tradeChallenge, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tradeChallenge);
        });

        it('should return initial array if no TradeChallenge is added', () => {
          const tradeChallengeCollection: ITradeChallenge[] = [{ id: 123 }];
          expectedResult = service.addTradeChallengeToCollectionIfMissing(tradeChallengeCollection, undefined, null);
          expect(expectedResult).toEqual(tradeChallengeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
