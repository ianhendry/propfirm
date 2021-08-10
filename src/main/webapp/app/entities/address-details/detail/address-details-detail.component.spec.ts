import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AddressDetailsDetailComponent } from './address-details-detail.component';

describe('Component Tests', () => {
  describe('AddressDetails Management Detail Component', () => {
    let comp: AddressDetailsDetailComponent;
    let fixture: ComponentFixture<AddressDetailsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AddressDetailsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ addressDetails: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AddressDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AddressDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load addressDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.addressDetails).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
