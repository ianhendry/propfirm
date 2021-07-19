jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMt4Account, Mt4Account } from '../mt-4-account.model';
import { Mt4AccountService } from '../service/mt-4-account.service';

import { Mt4AccountRoutingResolveService } from './mt-4-account-routing-resolve.service';

describe('Service Tests', () => {
  describe('Mt4Account routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: Mt4AccountRoutingResolveService;
    let service: Mt4AccountService;
    let resultMt4Account: IMt4Account | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(Mt4AccountRoutingResolveService);
      service = TestBed.inject(Mt4AccountService);
      resultMt4Account = undefined;
    });

    describe('resolve', () => {
      it('should return IMt4Account returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMt4Account = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMt4Account).toEqual({ id: 123 });
      });

      it('should return new IMt4Account if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMt4Account = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMt4Account).toEqual(new Mt4Account());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Mt4Account })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMt4Account = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMt4Account).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
