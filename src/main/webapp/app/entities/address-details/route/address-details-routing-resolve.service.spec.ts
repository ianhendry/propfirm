jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAddressDetails, AddressDetails } from '../address-details.model';
import { AddressDetailsService } from '../service/address-details.service';

import { AddressDetailsRoutingResolveService } from './address-details-routing-resolve.service';

describe('Service Tests', () => {
  describe('AddressDetails routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AddressDetailsRoutingResolveService;
    let service: AddressDetailsService;
    let resultAddressDetails: IAddressDetails | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AddressDetailsRoutingResolveService);
      service = TestBed.inject(AddressDetailsService);
      resultAddressDetails = undefined;
    });

    describe('resolve', () => {
      it('should return IAddressDetails returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAddressDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAddressDetails).toEqual({ id: 123 });
      });

      it('should return new IAddressDetails if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAddressDetails = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAddressDetails).toEqual(new AddressDetails());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AddressDetails })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAddressDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAddressDetails).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
