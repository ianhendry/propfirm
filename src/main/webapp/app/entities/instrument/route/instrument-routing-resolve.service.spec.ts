jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInstrument, Instrument } from '../instrument.model';
import { InstrumentService } from '../service/instrument.service';

import { InstrumentRoutingResolveService } from './instrument-routing-resolve.service';

describe('Service Tests', () => {
  describe('Instrument routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InstrumentRoutingResolveService;
    let service: InstrumentService;
    let resultInstrument: IInstrument | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InstrumentRoutingResolveService);
      service = TestBed.inject(InstrumentService);
      resultInstrument = undefined;
    });

    describe('resolve', () => {
      it('should return IInstrument returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInstrument = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInstrument).toEqual({ id: 123 });
      });

      it('should return new IInstrument if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInstrument = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInstrument).toEqual(new Instrument());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Instrument })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInstrument = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInstrument).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
