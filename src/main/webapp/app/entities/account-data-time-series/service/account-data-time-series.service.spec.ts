import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAccountDataTimeSeries, AccountDataTimeSeries } from '../account-data-time-series.model';

import { AccountDataTimeSeriesService } from './account-data-time-series.service';

describe('Service Tests', () => {
  describe('AccountDataTimeSeries Service', () => {
    let service: AccountDataTimeSeriesService;
    let httpMock: HttpTestingController;
    let elemDefault: IAccountDataTimeSeries;
    let expectedResult: IAccountDataTimeSeries | IAccountDataTimeSeries[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AccountDataTimeSeriesService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateStamp: currentDate,
        accountBalance: 0,
        accountEquity: 0,
        accountCredit: 0,
        accountFreeMargin: 0,
        accountStopoutLevel: 0,
        openLots: 0,
        openNumberOfTrades: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateStamp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AccountDataTimeSeries', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateStamp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateStamp: currentDate,
          },
          returnedFromService
        );

        service.create(new AccountDataTimeSeries()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AccountDataTimeSeries', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateStamp: currentDate.format(DATE_TIME_FORMAT),
            accountBalance: 1,
            accountEquity: 1,
            accountCredit: 1,
            accountFreeMargin: 1,
            accountStopoutLevel: 1,
            openLots: 1,
            openNumberOfTrades: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateStamp: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AccountDataTimeSeries', () => {
        const patchObject = Object.assign(
          {
            accountBalance: 1,
            accountStopoutLevel: 1,
          },
          new AccountDataTimeSeries()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateStamp: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AccountDataTimeSeries', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateStamp: currentDate.format(DATE_TIME_FORMAT),
            accountBalance: 1,
            accountEquity: 1,
            accountCredit: 1,
            accountFreeMargin: 1,
            accountStopoutLevel: 1,
            openLots: 1,
            openNumberOfTrades: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateStamp: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AccountDataTimeSeries', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAccountDataTimeSeriesToCollectionIfMissing', () => {
        it('should add a AccountDataTimeSeries to an empty array', () => {
          const accountDataTimeSeries: IAccountDataTimeSeries = { id: 123 };
          expectedResult = service.addAccountDataTimeSeriesToCollectionIfMissing([], accountDataTimeSeries);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(accountDataTimeSeries);
        });

        it('should not add a AccountDataTimeSeries to an array that contains it', () => {
          const accountDataTimeSeries: IAccountDataTimeSeries = { id: 123 };
          const accountDataTimeSeriesCollection: IAccountDataTimeSeries[] = [
            {
              ...accountDataTimeSeries,
            },
            { id: 456 },
          ];
          expectedResult = service.addAccountDataTimeSeriesToCollectionIfMissing(accountDataTimeSeriesCollection, accountDataTimeSeries);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AccountDataTimeSeries to an array that doesn't contain it", () => {
          const accountDataTimeSeries: IAccountDataTimeSeries = { id: 123 };
          const accountDataTimeSeriesCollection: IAccountDataTimeSeries[] = [{ id: 456 }];
          expectedResult = service.addAccountDataTimeSeriesToCollectionIfMissing(accountDataTimeSeriesCollection, accountDataTimeSeries);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(accountDataTimeSeries);
        });

        it('should add only unique AccountDataTimeSeries to an array', () => {
          const accountDataTimeSeriesArray: IAccountDataTimeSeries[] = [{ id: 123 }, { id: 456 }, { id: 49999 }];
          const accountDataTimeSeriesCollection: IAccountDataTimeSeries[] = [{ id: 123 }];
          expectedResult = service.addAccountDataTimeSeriesToCollectionIfMissing(
            accountDataTimeSeriesCollection,
            ...accountDataTimeSeriesArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const accountDataTimeSeries: IAccountDataTimeSeries = { id: 123 };
          const accountDataTimeSeries2: IAccountDataTimeSeries = { id: 456 };
          expectedResult = service.addAccountDataTimeSeriesToCollectionIfMissing([], accountDataTimeSeries, accountDataTimeSeries2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(accountDataTimeSeries);
          expect(expectedResult).toContain(accountDataTimeSeries2);
        });

        it('should accept null and undefined values', () => {
          const accountDataTimeSeries: IAccountDataTimeSeries = { id: 123 };
          expectedResult = service.addAccountDataTimeSeriesToCollectionIfMissing([], null, accountDataTimeSeries, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(accountDataTimeSeries);
        });

        it('should return initial array if no AccountDataTimeSeries is added', () => {
          const accountDataTimeSeriesCollection: IAccountDataTimeSeries[] = [{ id: 123 }];
          expectedResult = service.addAccountDataTimeSeriesToCollectionIfMissing(accountDataTimeSeriesCollection, undefined, null);
          expect(expectedResult).toEqual(accountDataTimeSeriesCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
