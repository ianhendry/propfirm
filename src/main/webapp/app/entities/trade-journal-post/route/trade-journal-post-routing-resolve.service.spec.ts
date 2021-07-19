jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITradeJournalPost, TradeJournalPost } from '../trade-journal-post.model';
import { TradeJournalPostService } from '../service/trade-journal-post.service';

import { TradeJournalPostRoutingResolveService } from './trade-journal-post-routing-resolve.service';

describe('Service Tests', () => {
  describe('TradeJournalPost routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TradeJournalPostRoutingResolveService;
    let service: TradeJournalPostService;
    let resultTradeJournalPost: ITradeJournalPost | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TradeJournalPostRoutingResolveService);
      service = TestBed.inject(TradeJournalPostService);
      resultTradeJournalPost = undefined;
    });

    describe('resolve', () => {
      it('should return ITradeJournalPost returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeJournalPost = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTradeJournalPost).toEqual({ id: 123 });
      });

      it('should return new ITradeJournalPost if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeJournalPost = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTradeJournalPost).toEqual(new TradeJournalPost());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TradeJournalPost })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTradeJournalPost = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTradeJournalPost).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
