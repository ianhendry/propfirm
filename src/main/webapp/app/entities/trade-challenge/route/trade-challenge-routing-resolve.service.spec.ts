jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITradeChallenge, TradeChallenge } from '../trade-challenge.model';
import { TradeChallengeService } from '../service/trade-challenge.service';

import { TradeChallengeRoutingResolveService } from './trade-challenge-routing-resolve.service';

describe('Service Tests', () => {
  describe('TradeChallenge routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TradeChallengeRoutingResolveService;
    let service: TradeChallengeService;
    let resultTradeChallenge: ITradeChallenge | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TradeChallengeRoutingResolveService);
      service = TestBed.inject(TradeChallengeService);
      resultTradeChallenge = undefined;
    });

    describe('resolve', () => {
      it('should return ITradeChallenge returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeChallenge = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTradeChallenge).toEqual({ id: 123 });
      });

      it('should return new ITradeChallenge if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeChallenge = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTradeChallenge).toEqual(new TradeChallenge());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TradeChallenge })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeChallenge = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTradeChallenge).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
