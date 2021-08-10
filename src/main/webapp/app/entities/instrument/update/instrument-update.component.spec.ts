jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InstrumentService } from '../service/instrument.service';
import { IInstrument, Instrument } from '../instrument.model';

import { InstrumentUpdateComponent } from './instrument-update.component';

describe('Component Tests', () => {
  describe('Instrument Management Update Component', () => {
    let comp: InstrumentUpdateComponent;
    let fixture: ComponentFixture<InstrumentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let instrumentService: InstrumentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InstrumentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InstrumentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InstrumentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      instrumentService = TestBed.inject(InstrumentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const instrument: IInstrument = { id: 456 };

        activatedRoute.data = of({ instrument });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(instrument));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Instrument>>();
        const instrument = { id: 123 };
        jest.spyOn(instrumentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ instrument });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: instrument }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(instrumentService.update).toHaveBeenCalledWith(instrument);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Instrument>>();
        const instrument = new Instrument();
        jest.spyOn(instrumentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ instrument });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: instrument }));
        saveSubject.complete();

        // THEN
        expect(instrumentService.create).toHaveBeenCalledWith(instrument);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Instrument>>();
        const instrument = { id: 123 };
        jest.spyOn(instrumentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ instrument });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(instrumentService.update).toHaveBeenCalledWith(instrument);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
