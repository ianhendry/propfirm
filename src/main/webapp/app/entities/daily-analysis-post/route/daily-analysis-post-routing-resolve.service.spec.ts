jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDailyAnalysisPost, DailyAnalysisPost } from '../daily-analysis-post.model';
import { DailyAnalysisPostService } from '../service/daily-analysis-post.service';

import { DailyAnalysisPostRoutingResolveService } from './daily-analysis-post-routing-resolve.service';

describe('Service Tests', () => {
  describe('DailyAnalysisPost routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DailyAnalysisPostRoutingResolveService;
    let service: DailyAnalysisPostService;
    let resultDailyAnalysisPost: IDailyAnalysisPost | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DailyAnalysisPostRoutingResolveService);
      service = TestBed.inject(DailyAnalysisPostService);
      resultDailyAnalysisPost = undefined;
    });

    describe('resolve', () => {
      it('should return IDailyAnalysisPost returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDailyAnalysisPost = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDailyAnalysisPost).toEqual({ id: 123 });
      });

      it('should return new IDailyAnalysisPost if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDailyAnalysisPost = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDailyAnalysisPost).toEqual(new DailyAnalysisPost());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DailyAnalysisPost })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDailyAnalysisPost = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDailyAnalysisPost).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
