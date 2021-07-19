jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { Mt4TradeService } from '../service/mt-4-trade.service';

import { Mt4TradeDeleteDialogComponent } from './mt-4-trade-delete-dialog.component';

describe('Component Tests', () => {
  describe('Mt4Trade Management Delete Component', () => {
    let comp: Mt4TradeDeleteDialogComponent;
    let fixture: ComponentFixture<Mt4TradeDeleteDialogComponent>;
    let service: Mt4TradeService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [Mt4TradeDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(Mt4TradeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Mt4TradeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(Mt4TradeService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
