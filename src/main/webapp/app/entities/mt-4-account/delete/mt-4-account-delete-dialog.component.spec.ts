jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { Mt4AccountService } from '../service/mt-4-account.service';

import { Mt4AccountDeleteDialogComponent } from './mt-4-account-delete-dialog.component';

describe('Component Tests', () => {
  describe('Mt4Account Management Delete Component', () => {
    let comp: Mt4AccountDeleteDialogComponent;
    let fixture: ComponentFixture<Mt4AccountDeleteDialogComponent>;
    let service: Mt4AccountService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [Mt4AccountDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(Mt4AccountDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Mt4AccountDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(Mt4AccountService);
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
