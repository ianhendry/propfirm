jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAccountDataTimeSeries, AccountDataTimeSeries } from '../account-data-time-series.model';
import { AccountDataTimeSeriesService } from '../service/account-data-time-series.service';

import { AccountDataTimeSeriesRoutingResolveService } from './account-data-time-series-routing-resolve.service';

describe('Service Tests', () => {
  describe('AccountDataTimeSeries routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AccountDataTimeSeriesRoutingResolveService;
    let service: AccountDataTimeSeriesService;
    let resultAccountDataTimeSeries: IAccountDataTimeSeries | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AccountDataTimeSeriesRoutingResolveService);
      service = TestBed.inject(AccountDataTimeSeriesService);
      resultAccountDataTimeSeries = undefined;
    });

    describe('resolve', () => {
      it('should return IAccountDataTimeSeries returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAccountDataTimeSeries = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAccountDataTimeSeries).toEqual({ id: 123 });
      });

      it('should return new IAccountDataTimeSeries if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAccountDataTimeSeries = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAccountDataTimeSeries).toEqual(new AccountDataTimeSeries());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AccountDataTimeSeries })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAccountDataTimeSeries = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAccountDataTimeSeries).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
