import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITradeJournalPost, TradeJournalPost } from '../trade-journal-post.model';

import { TradeJournalPostService } from './trade-journal-post.service';

describe('Service Tests', () => {
  describe('TradeJournalPost Service', () => {
    let service: TradeJournalPostService;
    let httpMock: HttpTestingController;
    let elemDefault: ITradeJournalPost;
    let expectedResult: ITradeJournalPost | ITradeJournalPost[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TradeJournalPostService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        postTitle: 'AAAAAAA',
        dateAdded: currentDate,
        thoughtsOnPsychology: 'AAAAAAA',
        thoughtsOnTradeProcessAccuracy: 'AAAAAAA',
        thoughtsOnAreasOfStrength: 'AAAAAAA',
        thoughtsOnAreasForImprovement: 'AAAAAAA',
        areaOfFocusForTomorrow: 'AAAAAAA',
        makePublicVisibleOnSite: false,
        anyMediaContentType: 'image/png',
        anyMedia: 'AAAAAAA',
        anyImageContentType: 'image/png',
        anyImage: 'AAAAAAA',
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

      it('should create a TradeJournalPost', () => {
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

        service.create(new TradeJournalPost()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TradeJournalPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            postTitle: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            thoughtsOnPsychology: 'BBBBBB',
            thoughtsOnTradeProcessAccuracy: 'BBBBBB',
            thoughtsOnAreasOfStrength: 'BBBBBB',
            thoughtsOnAreasForImprovement: 'BBBBBB',
            areaOfFocusForTomorrow: 'BBBBBB',
            makePublicVisibleOnSite: true,
            anyMedia: 'BBBBBB',
            anyImage: 'BBBBBB',
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

      it('should partial update a TradeJournalPost', () => {
        const patchObject = Object.assign(
          {
            postTitle: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            thoughtsOnAreasOfStrength: 'BBBBBB',
            thoughtsOnAreasForImprovement: 'BBBBBB',
            anyMedia: 'BBBBBB',
            anyImage: 'BBBBBB',
          },
          new TradeJournalPost()
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

      it('should return a list of TradeJournalPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            postTitle: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            thoughtsOnPsychology: 'BBBBBB',
            thoughtsOnTradeProcessAccuracy: 'BBBBBB',
            thoughtsOnAreasOfStrength: 'BBBBBB',
            thoughtsOnAreasForImprovement: 'BBBBBB',
            areaOfFocusForTomorrow: 'BBBBBB',
            makePublicVisibleOnSite: true,
            anyMedia: 'BBBBBB',
            anyImage: 'BBBBBB',
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

      it('should delete a TradeJournalPost', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTradeJournalPostToCollectionIfMissing', () => {
        it('should add a TradeJournalPost to an empty array', () => {
          const tradeJournalPost: ITradeJournalPost = { id: 123 };
          expectedResult = service.addTradeJournalPostToCollectionIfMissing([], tradeJournalPost);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tradeJournalPost);
        });

        it('should not add a TradeJournalPost to an array that contains it', () => {
          const tradeJournalPost: ITradeJournalPost = { id: 123 };
          const tradeJournalPostCollection: ITradeJournalPost[] = [
            {
              ...tradeJournalPost,
            },
            { id: 456 },
          ];
          expectedResult = service.addTradeJournalPostToCollectionIfMissing(tradeJournalPostCollection, tradeJournalPost);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TradeJournalPost to an array that doesn't contain it", () => {
          const tradeJournalPost: ITradeJournalPost = { id: 123 };
          const tradeJournalPostCollection: ITradeJournalPost[] = [{ id: 456 }];
          expectedResult = service.addTradeJournalPostToCollectionIfMissing(tradeJournalPostCollection, tradeJournalPost);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tradeJournalPost);
        });

        it('should add only unique TradeJournalPost to an array', () => {
          const tradeJournalPostArray: ITradeJournalPost[] = [{ id: 123 }, { id: 456 }, { id: 80420 }];
          const tradeJournalPostCollection: ITradeJournalPost[] = [{ id: 123 }];
          expectedResult = service.addTradeJournalPostToCollectionIfMissing(tradeJournalPostCollection, ...tradeJournalPostArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tradeJournalPost: ITradeJournalPost = { id: 123 };
          const tradeJournalPost2: ITradeJournalPost = { id: 456 };
          expectedResult = service.addTradeJournalPostToCollectionIfMissing([], tradeJournalPost, tradeJournalPost2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tradeJournalPost);
          expect(expectedResult).toContain(tradeJournalPost2);
        });

        it('should accept null and undefined values', () => {
          const tradeJournalPost: ITradeJournalPost = { id: 123 };
          expectedResult = service.addTradeJournalPostToCollectionIfMissing([], null, tradeJournalPost, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tradeJournalPost);
        });

        it('should return initial array if no TradeJournalPost is added', () => {
          const tradeJournalPostCollection: ITradeJournalPost[] = [{ id: 123 }];
          expectedResult = service.addTradeJournalPostToCollectionIfMissing(tradeJournalPostCollection, undefined, null);
          expect(expectedResult).toEqual(tradeJournalPostCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
