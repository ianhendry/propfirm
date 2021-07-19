jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMt4Trade, Mt4Trade } from '../mt-4-trade.model';
import { Mt4TradeService } from '../service/mt-4-trade.service';

import { Mt4TradeRoutingResolveService } from './mt-4-trade-routing-resolve.service';

describe('Service Tests', () => {
  describe('Mt4Trade routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: Mt4TradeRoutingResolveService;
    let service: Mt4TradeService;
    let resultMt4Trade: IMt4Trade | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(Mt4TradeRoutingResolveService);
      service = TestBed.inject(Mt4TradeService);
      resultMt4Trade = undefined;
    });

    describe('resolve', () => {
      it('should return IMt4Trade returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMt4Trade = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMt4Trade).toEqual({ id: 123 });
      });

      it('should return new IMt4Trade if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMt4Trade = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMt4Trade).toEqual(new Mt4Trade());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Mt4Trade })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMt4Trade = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMt4Trade).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
