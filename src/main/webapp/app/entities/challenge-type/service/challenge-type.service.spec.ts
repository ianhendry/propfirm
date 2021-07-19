import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IChallengeType, ChallengeType } from '../challenge-type.model';

import { ChallengeTypeService } from './challenge-type.service';

describe('Service Tests', () => {
  describe('ChallengeType Service', () => {
    let service: ChallengeTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IChallengeType;
    let expectedResult: IChallengeType | IChallengeType[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChallengeTypeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        challengeTypeName: 'AAAAAAA',
        priceContentType: 'image/png',
        price: 'AAAAAAA',
        refundAfterComplete: false,
        accountSize: 0,
        profitTargetPhaseOne: 0,
        profitTargetPhaseTwo: 0,
        durationDaysPhaseOne: 0,
        durationDaysPhaseTwo: 0,
        maxDailyDrawdown: 0,
        maxTotalDrawDown: 0,
        profitSplitRatio: 0,
        freeRetry: false,
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

      it('should create a ChallengeType', () => {
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

        service.create(new ChallengeType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ChallengeType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            challengeTypeName: 'BBBBBB',
            price: 'BBBBBB',
            refundAfterComplete: true,
            accountSize: 1,
            profitTargetPhaseOne: 1,
            profitTargetPhaseTwo: 1,
            durationDaysPhaseOne: 1,
            durationDaysPhaseTwo: 1,
            maxDailyDrawdown: 1,
            maxTotalDrawDown: 1,
            profitSplitRatio: 1,
            freeRetry: true,
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

      it('should partial update a ChallengeType', () => {
        const patchObject = Object.assign(
          {
            challengeTypeName: 'BBBBBB',
            price: 'BBBBBB',
            refundAfterComplete: true,
            accountSize: 1,
            profitTargetPhaseTwo: 1,
            durationDaysPhaseTwo: 1,
            maxDailyDrawdown: 1,
            profitSplitRatio: 1,
            inActive: true,
          },
          new ChallengeType()
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

      it('should return a list of ChallengeType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            challengeTypeName: 'BBBBBB',
            price: 'BBBBBB',
            refundAfterComplete: true,
            accountSize: 1,
            profitTargetPhaseOne: 1,
            profitTargetPhaseTwo: 1,
            durationDaysPhaseOne: 1,
            durationDaysPhaseTwo: 1,
            maxDailyDrawdown: 1,
            maxTotalDrawDown: 1,
            profitSplitRatio: 1,
            freeRetry: true,
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

      it('should delete a ChallengeType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChallengeTypeToCollectionIfMissing', () => {
        it('should add a ChallengeType to an empty array', () => {
          const challengeType: IChallengeType = { id: 123 };
          expectedResult = service.addChallengeTypeToCollectionIfMissing([], challengeType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(challengeType);
        });

        it('should not add a ChallengeType to an array that contains it', () => {
          const challengeType: IChallengeType = { id: 123 };
          const challengeTypeCollection: IChallengeType[] = [
            {
              ...challengeType,
            },
            { id: 456 },
          ];
          expectedResult = service.addChallengeTypeToCollectionIfMissing(challengeTypeCollection, challengeType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ChallengeType to an array that doesn't contain it", () => {
          const challengeType: IChallengeType = { id: 123 };
          const challengeTypeCollection: IChallengeType[] = [{ id: 456 }];
          expectedResult = service.addChallengeTypeToCollectionIfMissing(challengeTypeCollection, challengeType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(challengeType);
        });

        it('should add only unique ChallengeType to an array', () => {
          const challengeTypeArray: IChallengeType[] = [{ id: 123 }, { id: 456 }, { id: 79703 }];
          const challengeTypeCollection: IChallengeType[] = [{ id: 123 }];
          expectedResult = service.addChallengeTypeToCollectionIfMissing(challengeTypeCollection, ...challengeTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const challengeType: IChallengeType = { id: 123 };
          const challengeType2: IChallengeType = { id: 456 };
          expectedResult = service.addChallengeTypeToCollectionIfMissing([], challengeType, challengeType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(challengeType);
          expect(expectedResult).toContain(challengeType2);
        });

        it('should accept null and undefined values', () => {
          const challengeType: IChallengeType = { id: 123 };
          expectedResult = service.addChallengeTypeToCollectionIfMissing([], null, challengeType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(challengeType);
        });

        it('should return initial array if no ChallengeType is added', () => {
          const challengeTypeCollection: IChallengeType[] = [{ id: 123 }];
          expectedResult = service.addChallengeTypeToCollectionIfMissing(challengeTypeCollection, undefined, null);
          expect(expectedResult).toEqual(challengeTypeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
