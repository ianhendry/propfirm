import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AddressDetailsService } from '../service/address-details.service';

import { AddressDetailsComponent } from './address-details.component';

describe('Component Tests', () => {
  describe('AddressDetails Management Component', () => {
    let comp: AddressDetailsComponent;
    let fixture: ComponentFixture<AddressDetailsComponent>;
    let service: AddressDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AddressDetailsComponent],
      })
        .overrideTemplate(AddressDetailsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AddressDetailsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AddressDetailsService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.addressDetails?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
