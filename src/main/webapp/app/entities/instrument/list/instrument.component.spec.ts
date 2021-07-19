import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { InstrumentService } from '../service/instrument.service';

import { InstrumentComponent } from './instrument.component';

describe('Component Tests', () => {
  describe('Instrument Management Component', () => {
    let comp: InstrumentComponent;
    let fixture: ComponentFixture<InstrumentComponent>;
    let service: InstrumentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InstrumentComponent],
      })
        .overrideTemplate(InstrumentComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InstrumentComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(InstrumentService);

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
      expect(comp.instruments?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
