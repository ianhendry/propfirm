import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TRADEDIRECTION } from 'app/entities/enumerations/tradedirection.model';
import { IMt4Trade, Mt4Trade } from '../mt-4-trade.model';

import { Mt4TradeService } from './mt-4-trade.service';

describe('Service Tests', () => {
  describe('Mt4Trade Service', () => {
    let service: Mt4TradeService;
    let httpMock: HttpTestingController;
    let elemDefault: IMt4Trade;
    let expectedResult: IMt4Trade | IMt4Trade[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(Mt4TradeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        ticket: 0,
        openTime: currentDate,
        directionType: TRADEDIRECTION.BUY,
        positionSize: 0,
        symbol: 'AAAAAAA',
        openPrice: 0,
        stopLossPrice: 0,
        takeProfitPrice: 0,
        closeTime: currentDate,
        closePrice: 0,
        commission: 0,
        taxes: 0,
        swap: 0,
        profit: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            openTime: currentDate.format(DATE_TIME_FORMAT),
            closeTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Mt4Trade', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            openTime: currentDate.format(DATE_TIME_FORMAT),
            closeTime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            openTime: currentDate,
            closeTime: currentDate,
          },
          returnedFromService
        );

        service.create(new Mt4Trade()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Mt4Trade', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ticket: 1,
            openTime: currentDate.format(DATE_TIME_FORMAT),
            directionType: 'BBBBBB',
            positionSize: 1,
            symbol: 'BBBBBB',
            openPrice: 1,
            stopLossPrice: 1,
            takeProfitPrice: 1,
            closeTime: currentDate.format(DATE_TIME_FORMAT),
            closePrice: 1,
            commission: 1,
            taxes: 1,
            swap: 1,
            profit: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            openTime: currentDate,
            closeTime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Mt4Trade', () => {
        const patchObject = Object.assign(
          {
            ticket: 1,
            directionType: 'BBBBBB',
            positionSize: 1,
            takeProfitPrice: 1,
            closePrice: 1,
            commission: 1,
            taxes: 1,
            swap: 1,
          },
          new Mt4Trade()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            openTime: currentDate,
            closeTime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Mt4Trade', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ticket: 1,
            openTime: currentDate.format(DATE_TIME_FORMAT),
            directionType: 'BBBBBB',
            positionSize: 1,
            symbol: 'BBBBBB',
            openPrice: 1,
            stopLossPrice: 1,
            takeProfitPrice: 1,
            closeTime: currentDate.format(DATE_TIME_FORMAT),
            closePrice: 1,
            commission: 1,
            taxes: 1,
            swap: 1,
            profit: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            openTime: currentDate,
            closeTime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Mt4Trade', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMt4TradeToCollectionIfMissing', () => {
        it('should add a Mt4Trade to an empty array', () => {
          const mt4Trade: IMt4Trade = { id: 123 };
          expectedResult = service.addMt4TradeToCollectionIfMissing([], mt4Trade);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mt4Trade);
        });

        it('should not add a Mt4Trade to an array that contains it', () => {
          const mt4Trade: IMt4Trade = { id: 123 };
          const mt4TradeCollection: IMt4Trade[] = [
            {
              ...mt4Trade,
            },
            { id: 456 },
          ];
          expectedResult = service.addMt4TradeToCollectionIfMissing(mt4TradeCollection, mt4Trade);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Mt4Trade to an array that doesn't contain it", () => {
          const mt4Trade: IMt4Trade = { id: 123 };
          const mt4TradeCollection: IMt4Trade[] = [{ id: 456 }];
          expectedResult = service.addMt4TradeToCollectionIfMissing(mt4TradeCollection, mt4Trade);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mt4Trade);
        });

        it('should add only unique Mt4Trade to an array', () => {
          const mt4TradeArray: IMt4Trade[] = [{ id: 123 }, { id: 456 }, { id: 69703 }];
          const mt4TradeCollection: IMt4Trade[] = [{ id: 123 }];
          expectedResult = service.addMt4TradeToCollectionIfMissing(mt4TradeCollection, ...mt4TradeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mt4Trade: IMt4Trade = { id: 123 };
          const mt4Trade2: IMt4Trade = { id: 456 };
          expectedResult = service.addMt4TradeToCollectionIfMissing([], mt4Trade, mt4Trade2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mt4Trade);
          expect(expectedResult).toContain(mt4Trade2);
        });

        it('should accept null and undefined values', () => {
          const mt4Trade: IMt4Trade = { id: 123 };
          expectedResult = service.addMt4TradeToCollectionIfMissing([], null, mt4Trade, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mt4Trade);
        });

        it('should return initial array if no Mt4Trade is added', () => {
          const mt4TradeCollection: IMt4Trade[] = [{ id: 123 }];
          expectedResult = service.addMt4TradeToCollectionIfMissing(mt4TradeCollection, undefined, null);
          expect(expectedResult).toEqual(mt4TradeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
