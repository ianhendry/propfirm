import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDailyAnalysisPost, DailyAnalysisPost } from '../daily-analysis-post.model';

import { DailyAnalysisPostService } from './daily-analysis-post.service';

describe('Service Tests', () => {
  describe('DailyAnalysisPost Service', () => {
    let service: DailyAnalysisPostService;
    let httpMock: HttpTestingController;
    let elemDefault: IDailyAnalysisPost;
    let expectedResult: IDailyAnalysisPost | IDailyAnalysisPost[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DailyAnalysisPostService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        postTitle: 'AAAAAAA',
        dateAdded: currentDate,
        backgroundVolume: 'AAAAAAA',
        overallThoughts: 'AAAAAAA',
        weeklyChartContentType: 'image/png',
        weeklyChart: 'AAAAAAA',
        dailyChartContentType: 'image/png',
        dailyChart: 'AAAAAAA',
        oneHrChartContentType: 'image/png',
        oneHrChart: 'AAAAAAA',
        planForToday: 'AAAAAAA',
        makePublicVisibleOnSite: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a DailyAnalysisPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAdded: currentDate,
          },
          returnedFromService
        );

        service.create(new DailyAnalysisPost()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DailyAnalysisPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            postTitle: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            backgroundVolume: 'BBBBBB',
            overallThoughts: 'BBBBBB',
            weeklyChart: 'BBBBBB',
            dailyChart: 'BBBBBB',
            oneHrChart: 'BBBBBB',
            planForToday: 'BBBBBB',
            makePublicVisibleOnSite: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAdded: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DailyAnalysisPost', () => {
        const patchObject = Object.assign(
          {
            postTitle: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            backgroundVolume: 'BBBBBB',
            weeklyChart: 'BBBBBB',
            oneHrChart: 'BBBBBB',
          },
          new DailyAnalysisPost()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateAdded: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DailyAnalysisPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            postTitle: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            backgroundVolume: 'BBBBBB',
            overallThoughts: 'BBBBBB',
            weeklyChart: 'BBBBBB',
            dailyChart: 'BBBBBB',
            oneHrChart: 'BBBBBB',
            planForToday: 'BBBBBB',
            makePublicVisibleOnSite: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAdded: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a DailyAnalysisPost', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDailyAnalysisPostToCollectionIfMissing', () => {
        it('should add a DailyAnalysisPost to an empty array', () => {
          const dailyAnalysisPost: IDailyAnalysisPost = { id: 123 };
          expectedResult = service.addDailyAnalysisPostToCollectionIfMissing([], dailyAnalysisPost);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dailyAnalysisPost);
        });

        it('should not add a DailyAnalysisPost to an array that contains it', () => {
          const dailyAnalysisPost: IDailyAnalysisPost = { id: 123 };
          const dailyAnalysisPostCollection: IDailyAnalysisPost[] = [
            {
              ...dailyAnalysisPost,
            },
            { id: 456 },
          ];
          expectedResult = service.addDailyAnalysisPostToCollectionIfMissing(dailyAnalysisPostCollection, dailyAnalysisPost);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DailyAnalysisPost to an array that doesn't contain it", () => {
          const dailyAnalysisPost: IDailyAnalysisPost = { id: 123 };
          const dailyAnalysisPostCollection: IDailyAnalysisPost[] = [{ id: 456 }];
          expectedResult = service.addDailyAnalysisPostToCollectionIfMissing(dailyAnalysisPostCollection, dailyAnalysisPost);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dailyAnalysisPost);
        });

        it('should add only unique DailyAnalysisPost to an array', () => {
          const dailyAnalysisPostArray: IDailyAnalysisPost[] = [{ id: 123 }, { id: 456 }, { id: 73903 }];
          const dailyAnalysisPostCollection: IDailyAnalysisPost[] = [{ id: 123 }];
          expectedResult = service.addDailyAnalysisPostToCollectionIfMissing(dailyAnalysisPostCollection, ...dailyAnalysisPostArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dailyAnalysisPost: IDailyAnalysisPost = { id: 123 };
          const dailyAnalysisPost2: IDailyAnalysisPost = { id: 456 };
          expectedResult = service.addDailyAnalysisPostToCollectionIfMissing([], dailyAnalysisPost, dailyAnalysisPost2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dailyAnalysisPost);
          expect(expectedResult).toContain(dailyAnalysisPost2);
        });

        it('should accept null and undefined values', () => {
          const dailyAnalysisPost: IDailyAnalysisPost = { id: 123 };
          expectedResult = service.addDailyAnalysisPostToCollectionIfMissing([], null, dailyAnalysisPost, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dailyAnalysisPost);
        });

        it('should return initial array if no DailyAnalysisPost is added', () => {
          const dailyAnalysisPostCollection: IDailyAnalysisPost[] = [{ id: 123 }];
          expectedResult = service.addDailyAnalysisPostToCollectionIfMissing(dailyAnalysisPostCollection, undefined, null);
          expect(expectedResult).toEqual(dailyAnalysisPostCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
