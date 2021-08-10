import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserComment, UserComment } from '../user-comment.model';

import { UserCommentService } from './user-comment.service';

describe('Service Tests', () => {
  describe('UserComment Service', () => {
    let service: UserCommentService;
    let httpMock: HttpTestingController;
    let elemDefault: IUserComment;
    let expectedResult: IUserComment | IUserComment[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UserCommentService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        commentTitle: 'AAAAAAA',
        commentBody: 'AAAAAAA',
        commentMediaContentType: 'image/png',
        commentMedia: 'AAAAAAA',
        dateAdded: currentDate,
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

      it('should create a UserComment', () => {
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

        service.create(new UserComment()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UserComment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            commentTitle: 'BBBBBB',
            commentBody: 'BBBBBB',
            commentMedia: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
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

      it('should partial update a UserComment', () => {
        const patchObject = Object.assign(
          {
            commentMedia: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
          },
          new UserComment()
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

      it('should return a list of UserComment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            commentTitle: 'BBBBBB',
            commentBody: 'BBBBBB',
            commentMedia: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
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

      it('should delete a UserComment', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUserCommentToCollectionIfMissing', () => {
        it('should add a UserComment to an empty array', () => {
          const userComment: IUserComment = { id: 123 };
          expectedResult = service.addUserCommentToCollectionIfMissing([], userComment);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userComment);
        });

        it('should not add a UserComment to an array that contains it', () => {
          const userComment: IUserComment = { id: 123 };
          const userCommentCollection: IUserComment[] = [
            {
              ...userComment,
            },
            { id: 456 },
          ];
          expectedResult = service.addUserCommentToCollectionIfMissing(userCommentCollection, userComment);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UserComment to an array that doesn't contain it", () => {
          const userComment: IUserComment = { id: 123 };
          const userCommentCollection: IUserComment[] = [{ id: 456 }];
          expectedResult = service.addUserCommentToCollectionIfMissing(userCommentCollection, userComment);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userComment);
        });

        it('should add only unique UserComment to an array', () => {
          const userCommentArray: IUserComment[] = [{ id: 123 }, { id: 456 }, { id: 21207 }];
          const userCommentCollection: IUserComment[] = [{ id: 123 }];
          expectedResult = service.addUserCommentToCollectionIfMissing(userCommentCollection, ...userCommentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const userComment: IUserComment = { id: 123 };
          const userComment2: IUserComment = { id: 456 };
          expectedResult = service.addUserCommentToCollectionIfMissing([], userComment, userComment2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userComment);
          expect(expectedResult).toContain(userComment2);
        });

        it('should accept null and undefined values', () => {
          const userComment: IUserComment = { id: 123 };
          expectedResult = service.addUserCommentToCollectionIfMissing([], null, userComment, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userComment);
        });

        it('should return initial array if no UserComment is added', () => {
          const userCommentCollection: IUserComment[] = [{ id: 123 }];
          expectedResult = service.addUserCommentToCollectionIfMissing(userCommentCollection, undefined, null);
          expect(expectedResult).toEqual(userCommentCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
