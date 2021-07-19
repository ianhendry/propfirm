jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IChallengeType, ChallengeType } from '../challenge-type.model';
import { ChallengeTypeService } from '../service/challenge-type.service';

import { ChallengeTypeRoutingResolveService } from './challenge-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('ChallengeType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ChallengeTypeRoutingResolveService;
    let service: ChallengeTypeService;
    let resultChallengeType: IChallengeType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ChallengeTypeRoutingResolveService);
      service = TestBed.inject(ChallengeTypeService);
      resultChallengeType = undefined;
    });

    describe('resolve', () => {
      it('should return IChallengeType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChallengeType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChallengeType).toEqual({ id: 123 });
      });

      it('should return new IChallengeType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChallengeType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultChallengeType).toEqual(new ChallengeType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ChallengeType })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChallengeType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChallengeType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
