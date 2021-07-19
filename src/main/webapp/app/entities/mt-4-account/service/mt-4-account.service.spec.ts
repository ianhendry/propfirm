import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ACCOUNTTYPE } from 'app/entities/enumerations/accounttype.model';
import { BROKER } from 'app/entities/enumerations/broker.model';
import { IMt4Account, Mt4Account } from '../mt-4-account.model';

import { Mt4AccountService } from './mt-4-account.service';

describe('Service Tests', () => {
  describe('Mt4Account Service', () => {
    let service: Mt4AccountService;
    let httpMock: HttpTestingController;
    let elemDefault: IMt4Account;
    let expectedResult: IMt4Account | IMt4Account[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(Mt4AccountService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        accountType: ACCOUNTTYPE.REAL,
        accountBroker: BROKER.FXPRO,
        accountLogin: 'AAAAAAA',
        accountPassword: 'AAAAAAA',
        accountInvestorLogin: 'AAAAAAA',
        accountInvestorPassword: 'AAAAAAA',
        accountReal: false,
        accountInfoDouble: 0,
        accountInfoInteger: 0,
        accountInfoString: 'AAAAAAA',
        accountBalance: 0,
        accountCredit: 0,
        accountCompany: 'AAAAAAA',
        accountCurrency: 'AAAAAAA',
        accountEquity: 0,
        accountFreeMargin: 0,
        accountFreeMarginCheck: 0,
        accountFreeMarginMode: 0,
        accountLeverage: 0,
        accountMargin: 0,
        accountName: 'AAAAAAA',
        accountNumber: 0,
        accountProfit: 0,
        accountServer: 'AAAAAAA',
        accountStopoutLevel: 0,
        accountStopoutMode: 0,
        inActive: false,
        inActiveDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Mt4Account', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Mt4Account()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Mt4Account', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            accountType: 'BBBBBB',
            accountBroker: 'BBBBBB',
            accountLogin: 'BBBBBB',
            accountPassword: 'BBBBBB',
            accountInvestorLogin: 'BBBBBB',
            accountInvestorPassword: 'BBBBBB',
            accountReal: true,
            accountInfoDouble: 1,
            accountInfoInteger: 1,
            accountInfoString: 'BBBBBB',
            accountBalance: 1,
            accountCredit: 1,
            accountCompany: 'BBBBBB',
            accountCurrency: 'BBBBBB',
            accountEquity: 1,
            accountFreeMargin: 1,
            accountFreeMarginCheck: 1,
            accountFreeMarginMode: 1,
            accountLeverage: 1,
            accountMargin: 1,
            accountName: 'BBBBBB',
            accountNumber: 1,
            accountProfit: 1,
            accountServer: 'BBBBBB',
            accountStopoutLevel: 1,
            accountStopoutMode: 1,
            inActive: true,
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Mt4Account', () => {
        const patchObject = Object.assign(
          {
            accountType: 'BBBBBB',
            accountPassword: 'BBBBBB',
            accountInvestorLogin: 'BBBBBB',
            accountInfoDouble: 1,
            accountInfoInteger: 1,
            accountInfoString: 'BBBBBB',
            accountFreeMarginCheck: 1,
            accountLeverage: 1,
            accountMargin: 1,
            accountNumber: 1,
            accountStopoutLevel: 1,
            accountStopoutMode: 1,
          },
          new Mt4Account()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Mt4Account', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            accountType: 'BBBBBB',
            accountBroker: 'BBBBBB',
            accountLogin: 'BBBBBB',
            accountPassword: 'BBBBBB',
            accountInvestorLogin: 'BBBBBB',
            accountInvestorPassword: 'BBBBBB',
            accountReal: true,
            accountInfoDouble: 1,
            accountInfoInteger: 1,
            accountInfoString: 'BBBBBB',
            accountBalance: 1,
            accountCredit: 1,
            accountCompany: 'BBBBBB',
            accountCurrency: 'BBBBBB',
            accountEquity: 1,
            accountFreeMargin: 1,
            accountFreeMarginCheck: 1,
            accountFreeMarginMode: 1,
            accountLeverage: 1,
            accountMargin: 1,
            accountName: 'BBBBBB',
            accountNumber: 1,
            accountProfit: 1,
            accountServer: 'BBBBBB',
            accountStopoutLevel: 1,
            accountStopoutMode: 1,
            inActive: true,
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Mt4Account', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMt4AccountToCollectionIfMissing', () => {
        it('should add a Mt4Account to an empty array', () => {
          const mt4Account: IMt4Account = { id: 123 };
          expectedResult = service.addMt4AccountToCollectionIfMissing([], mt4Account);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mt4Account);
        });

        it('should not add a Mt4Account to an array that contains it', () => {
          const mt4Account: IMt4Account = { id: 123 };
          const mt4AccountCollection: IMt4Account[] = [
            {
              ...mt4Account,
            },
            { id: 456 },
          ];
          expectedResult = service.addMt4AccountToCollectionIfMissing(mt4AccountCollection, mt4Account);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Mt4Account to an array that doesn't contain it", () => {
          const mt4Account: IMt4Account = { id: 123 };
          const mt4AccountCollection: IMt4Account[] = [{ id: 456 }];
          expectedResult = service.addMt4AccountToCollectionIfMissing(mt4AccountCollection, mt4Account);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mt4Account);
        });

        it('should add only unique Mt4Account to an array', () => {
          const mt4AccountArray: IMt4Account[] = [{ id: 123 }, { id: 456 }, { id: 56700 }];
          const mt4AccountCollection: IMt4Account[] = [{ id: 123 }];
          expectedResult = service.addMt4AccountToCollectionIfMissing(mt4AccountCollection, ...mt4AccountArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mt4Account: IMt4Account = { id: 123 };
          const mt4Account2: IMt4Account = { id: 456 };
          expectedResult = service.addMt4AccountToCollectionIfMissing([], mt4Account, mt4Account2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mt4Account);
          expect(expectedResult).toContain(mt4Account2);
        });

        it('should accept null and undefined values', () => {
          const mt4Account: IMt4Account = { id: 123 };
          expectedResult = service.addMt4AccountToCollectionIfMissing([], null, mt4Account, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mt4Account);
        });

        it('should return initial array if no Mt4Account is added', () => {
          const mt4AccountCollection: IMt4Account[] = [{ id: 123 }];
          expectedResult = service.addMt4AccountToCollectionIfMissing(mt4AccountCollection, undefined, null);
          expect(expectedResult).toEqual(mt4AccountCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
