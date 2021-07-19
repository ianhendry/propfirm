import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INSTRUMENTTYPE } from 'app/entities/enumerations/instrumenttype.model';
import { IInstrument, Instrument } from '../instrument.model';

import { InstrumentService } from './instrument.service';

describe('Service Tests', () => {
  describe('Instrument Service', () => {
    let service: InstrumentService;
    let httpMock: HttpTestingController;
    let elemDefault: IInstrument;
    let expectedResult: IInstrument | IInstrument[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InstrumentService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        ticker: 'AAAAAAA',
        instrumentType: INSTRUMENTTYPE.CFD,
        exchange: 'AAAAAAA',
        averageSpread: 0,
        tradeRestrictions: 'AAAAAAA',
        inActive: false,
        inActiveDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Instrument', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Instrument()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Instrument', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ticker: 'BBBBBB',
            instrumentType: 'BBBBBB',
            exchange: 'BBBBBB',
            averageSpread: 1,
            tradeRestrictions: 'BBBBBB',
            inActive: true,
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Instrument', () => {
        const patchObject = Object.assign(
          {
            averageSpread: 1,
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new Instrument()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Instrument', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            ticker: 'BBBBBB',
            instrumentType: 'BBBBBB',
            exchange: 'BBBBBB',
            averageSpread: 1,
            tradeRestrictions: 'BBBBBB',
            inActive: true,
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Instrument', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInstrumentToCollectionIfMissing', () => {
        it('should add a Instrument to an empty array', () => {
          const instrument: IInstrument = { id: 123 };
          expectedResult = service.addInstrumentToCollectionIfMissing([], instrument);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(instrument);
        });

        it('should not add a Instrument to an array that contains it', () => {
          const instrument: IInstrument = { id: 123 };
          const instrumentCollection: IInstrument[] = [
            {
              ...instrument,
            },
            { id: 456 },
          ];
          expectedResult = service.addInstrumentToCollectionIfMissing(instrumentCollection, instrument);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Instrument to an array that doesn't contain it", () => {
          const instrument: IInstrument = { id: 123 };
          const instrumentCollection: IInstrument[] = [{ id: 456 }];
          expectedResult = service.addInstrumentToCollectionIfMissing(instrumentCollection, instrument);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(instrument);
        });

        it('should add only unique Instrument to an array', () => {
          const instrumentArray: IInstrument[] = [{ id: 123 }, { id: 456 }, { id: 46273 }];
          const instrumentCollection: IInstrument[] = [{ id: 123 }];
          expectedResult = service.addInstrumentToCollectionIfMissing(instrumentCollection, ...instrumentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const instrument: IInstrument = { id: 123 };
          const instrument2: IInstrument = { id: 456 };
          expectedResult = service.addInstrumentToCollectionIfMissing([], instrument, instrument2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(instrument);
          expect(expectedResult).toContain(instrument2);
        });

        it('should accept null and undefined values', () => {
          const instrument: IInstrument = { id: 123 };
          expectedResult = service.addInstrumentToCollectionIfMissing([], null, instrument, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(instrument);
        });

        it('should return initial array if no Instrument is added', () => {
          const instrumentCollection: IInstrument[] = [{ id: 123 }];
          expectedResult = service.addInstrumentToCollectionIfMissing(instrumentCollection, undefined, null);
          expect(expectedResult).toEqual(instrumentCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
