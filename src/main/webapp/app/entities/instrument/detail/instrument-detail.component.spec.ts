import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { InstrumentDetailComponent } from './instrument-detail.component';

describe('Component Tests', () => {
  describe('Instrument Management Detail Component', () => {
    let comp: InstrumentDetailComponent;
    let fixture: ComponentFixture<InstrumentDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InstrumentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ instrument: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(InstrumentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InstrumentDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
      jest.spyOn(window, 'open').mockImplementation(() => null);
    });

    describe('OnInit', () => {
      it('Should load instrument on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.instrument).toEqual(expect.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        jest.spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        jest.spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
