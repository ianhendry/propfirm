jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChallengeTypeService } from '../service/challenge-type.service';
import { IChallengeType, ChallengeType } from '../challenge-type.model';

import { ChallengeTypeUpdateComponent } from './challenge-type-update.component';

describe('Component Tests', () => {
  describe('ChallengeType Management Update Component', () => {
    let comp: ChallengeTypeUpdateComponent;
    let fixture: ComponentFixture<ChallengeTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let challengeTypeService: ChallengeTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChallengeTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ChallengeTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChallengeTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      challengeTypeService = TestBed.inject(ChallengeTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const challengeType: IChallengeType = { id: 456 };

        activatedRoute.data = of({ challengeType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(challengeType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChallengeType>>();
        const challengeType = { id: 123 };
        jest.spyOn(challengeTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ challengeType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: challengeType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(challengeTypeService.update).toHaveBeenCalledWith(challengeType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChallengeType>>();
        const challengeType = new ChallengeType();
        jest.spyOn(challengeTypeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ challengeType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: challengeType }));
        saveSubject.complete();

        // THEN
        expect(challengeTypeService.create).toHaveBeenCalledWith(challengeType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChallengeType>>();
        const challengeType = { id: 123 };
        jest.spyOn(challengeTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ challengeType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(challengeTypeService.update).toHaveBeenCalledWith(challengeType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
