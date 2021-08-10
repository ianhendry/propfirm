import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAddressDetails, AddressDetails } from '../address-details.model';

import { AddressDetailsService } from './address-details.service';

describe('Service Tests', () => {
  describe('AddressDetails Service', () => {
    let service: AddressDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IAddressDetails;
    let expectedResult: IAddressDetails | IAddressDetails[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AddressDetailsService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        contactName: 'AAAAAAA',
        address1: 'AAAAAAA',
        address2: 'AAAAAAA',
        address3: 'AAAAAAA',
        address4: 'AAAAAAA',
        address5: 'AAAAAAA',
        address6: 'AAAAAAA',
        dialCode: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
        messengerId: 'AAAAAAA',
        dateAdded: currentDate,
        inActive: false,
        inActiveDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AddressDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAdded: currentDate,
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.create(new AddressDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AddressDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            contactName: 'BBBBBB',
            address1: 'BBBBBB',
            address2: 'BBBBBB',
            address3: 'BBBBBB',
            address4: 'BBBBBB',
            address5: 'BBBBBB',
            address6: 'BBBBBB',
            dialCode: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            messengerId: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            inActive: true,
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAdded: currentDate,
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AddressDetails', () => {
        const patchObject = Object.assign(
          {
            contactName: 'BBBBBB',
            address1: 'BBBBBB',
            dialCode: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new AddressDetails()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateAdded: currentDate,
            inActiveDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AddressDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            contactName: 'BBBBBB',
            address1: 'BBBBBB',
            address2: 'BBBBBB',
            address3: 'BBBBBB',
            address4: 'BBBBBB',
            address5: 'BBBBBB',
            address6: 'BBBBBB',
            dialCode: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            messengerId: 'BBBBBB',
            dateAdded: currentDate.format(DATE_TIME_FORMAT),
            inActive: true,
            inActiveDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAdded: currentDate,
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

      it('should delete a AddressDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAddressDetailsToCollectionIfMissing', () => {
        it('should add a AddressDetails to an empty array', () => {
          const addressDetails: IAddressDetails = { id: 123 };
          expectedResult = service.addAddressDetailsToCollectionIfMissing([], addressDetails);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(addressDetails);
        });

        it('should not add a AddressDetails to an array that contains it', () => {
          const addressDetails: IAddressDetails = { id: 123 };
          const addressDetailsCollection: IAddressDetails[] = [
            {
              ...addressDetails,
            },
            { id: 456 },
          ];
          expectedResult = service.addAddressDetailsToCollectionIfMissing(addressDetailsCollection, addressDetails);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AddressDetails to an array that doesn't contain it", () => {
          const addressDetails: IAddressDetails = { id: 123 };
          const addressDetailsCollection: IAddressDetails[] = [{ id: 456 }];
          expectedResult = service.addAddressDetailsToCollectionIfMissing(addressDetailsCollection, addressDetails);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(addressDetails);
        });

        it('should add only unique AddressDetails to an array', () => {
          const addressDetailsArray: IAddressDetails[] = [{ id: 123 }, { id: 456 }, { id: 24087 }];
          const addressDetailsCollection: IAddressDetails[] = [{ id: 123 }];
          expectedResult = service.addAddressDetailsToCollectionIfMissing(addressDetailsCollection, ...addressDetailsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const addressDetails: IAddressDetails = { id: 123 };
          const addressDetails2: IAddressDetails = { id: 456 };
          expectedResult = service.addAddressDetailsToCollectionIfMissing([], addressDetails, addressDetails2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(addressDetails);
          expect(expectedResult).toContain(addressDetails2);
        });

        it('should accept null and undefined values', () => {
          const addressDetails: IAddressDetails = { id: 123 };
          expectedResult = service.addAddressDetailsToCollectionIfMissing([], null, addressDetails, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(addressDetails);
        });

        it('should return initial array if no AddressDetails is added', () => {
          const addressDetailsCollection: IAddressDetails[] = [{ id: 123 }];
          expectedResult = service.addAddressDetailsToCollectionIfMissing(addressDetailsCollection, undefined, null);
          expect(expectedResult).toEqual(addressDetailsCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
