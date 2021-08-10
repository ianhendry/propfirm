jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUserComment, UserComment } from '../user-comment.model';
import { UserCommentService } from '../service/user-comment.service';

import { UserCommentRoutingResolveService } from './user-comment-routing-resolve.service';

describe('Service Tests', () => {
  describe('UserComment routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UserCommentRoutingResolveService;
    let service: UserCommentService;
    let resultUserComment: IUserComment | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UserCommentRoutingResolveService);
      service = TestBed.inject(UserCommentService);
      resultUserComment = undefined;
    });

    describe('resolve', () => {
      it('should return IUserComment returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserComment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserComment).toEqual({ id: 123 });
      });

      it('should return new IUserComment if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserComment = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUserComment).toEqual(new UserComment());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UserComment })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserComment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserComment).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
