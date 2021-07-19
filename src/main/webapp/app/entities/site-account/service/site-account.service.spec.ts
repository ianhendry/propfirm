import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISiteAccount, SiteAccount } from '../site-account.model';

import { SiteAccountService } from './site-account.service';

describe('Service Tests', () => {
  describe('SiteAccount Service', () => {
    let service: SiteAccountService;
    let httpMock: HttpTestingController;
    let elemDefault: ISiteAccount;
    let expectedResult: ISiteAccount | ISiteAccount[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SiteAccountService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        accountName: 'AAAAAAA',
        userPictureContentType: 'image/png',
        userPicture: 'AAAAAAA',
        userBio: 'AAAAAAA',
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

      it('should create a SiteAccount', () => {
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

        service.create(new SiteAccount()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SiteAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            accountName: 'BBBBBB',
            userPicture: 'BBBBBB',
            userBio: 'BBBBBB',
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

      it('should partial update a SiteAccount', () => {
        const patchObject = Object.assign(
          {
            accountName: 'BBBBBB',
            userPicture: 'BBBBBB',
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new SiteAccount()
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

      it('should return a list of SiteAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            accountName: 'BBBBBB',
            userPicture: 'BBBBBB',
            userBio: 'BBBBBB',
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

      it('should delete a SiteAccount', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSiteAccountToCollectionIfMissing', () => {
        it('should add a SiteAccount to an empty array', () => {
          const siteAccount: ISiteAccount = { id: 123 };
          expectedResult = service.addSiteAccountToCollectionIfMissing([], siteAccount);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(siteAccount);
        });

        it('should not add a SiteAccount to an array that contains it', () => {
          const siteAccount: ISiteAccount = { id: 123 };
          const siteAccountCollection: ISiteAccount[] = [
            {
              ...siteAccount,
            },
            { id: 456 },
          ];
          expectedResult = service.addSiteAccountToCollectionIfMissing(siteAccountCollection, siteAccount);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SiteAccount to an array that doesn't contain it", () => {
          const siteAccount: ISiteAccount = { id: 123 };
          const siteAccountCollection: ISiteAccount[] = [{ id: 456 }];
          expectedResult = service.addSiteAccountToCollectionIfMissing(siteAccountCollection, siteAccount);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(siteAccount);
        });

        it('should add only unique SiteAccount to an array', () => {
          const siteAccountArray: ISiteAccount[] = [{ id: 123 }, { id: 456 }, { id: 17528 }];
          const siteAccountCollection: ISiteAccount[] = [{ id: 123 }];
          expectedResult = service.addSiteAccountToCollectionIfMissing(siteAccountCollection, ...siteAccountArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const siteAccount: ISiteAccount = { id: 123 };
          const siteAccount2: ISiteAccount = { id: 456 };
          expectedResult = service.addSiteAccountToCollectionIfMissing([], siteAccount, siteAccount2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(siteAccount);
          expect(expectedResult).toContain(siteAccount2);
        });

        it('should accept null and undefined values', () => {
          const siteAccount: ISiteAccount = { id: 123 };
          expectedResult = service.addSiteAccountToCollectionIfMissing([], null, siteAccount, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(siteAccount);
        });

        it('should return initial array if no SiteAccount is added', () => {
          const siteAccountCollection: ISiteAccount[] = [{ id: 123 }];
          expectedResult = service.addSiteAccountToCollectionIfMissing(siteAccountCollection, undefined, null);
          expect(expectedResult).toEqual(siteAccountCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
