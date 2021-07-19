jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DailyAnalysisPostService } from '../service/daily-analysis-post.service';
import { IDailyAnalysisPost, DailyAnalysisPost } from '../daily-analysis-post.model';
import { IInstrument } from 'app/entities/instrument/instrument.model';
import { InstrumentService } from 'app/entities/instrument/service/instrument.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { DailyAnalysisPostUpdateComponent } from './daily-analysis-post-update.component';

describe('Component Tests', () => {
  describe('DailyAnalysisPost Management Update Component', () => {
    let comp: DailyAnalysisPostUpdateComponent;
    let fixture: ComponentFixture<DailyAnalysisPostUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dailyAnalysisPostService: DailyAnalysisPostService;
    let instrumentService: InstrumentService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DailyAnalysisPostUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DailyAnalysisPostUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DailyAnalysisPostUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dailyAnalysisPostService = TestBed.inject(DailyAnalysisPostService);
      instrumentService = TestBed.inject(InstrumentService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Instrument query and add missing value', () => {
        const dailyAnalysisPost: IDailyAnalysisPost = { id: 456 };
        const instrument: IInstrument = { id: 41000 };
        dailyAnalysisPost.instrument = instrument;

        const instrumentCollection: IInstrument[] = [{ id: 42678 }];
        jest.spyOn(instrumentService, 'query').mockReturnValue(of(new HttpResponse({ body: instrumentCollection })));
        const additionalInstruments = [instrument];
        const expectedCollection: IInstrument[] = [...additionalInstruments, ...instrumentCollection];
        jest.spyOn(instrumentService, 'addInstrumentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dailyAnalysisPost });
        comp.ngOnInit();

        expect(instrumentService.query).toHaveBeenCalled();
        expect(instrumentService.addInstrumentToCollectionIfMissing).toHaveBeenCalledWith(instrumentCollection, ...additionalInstruments);
        expect(comp.instrumentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const dailyAnalysisPost: IDailyAnalysisPost = { id: 456 };
        const user: IUser = { id: 48503 };
        dailyAnalysisPost.user = user;

        const userCollection: IUser[] = [{ id: 34922 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dailyAnalysisPost });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dailyAnalysisPost: IDailyAnalysisPost = { id: 456 };
        const instrument: IInstrument = { id: 2614 };
        dailyAnalysisPost.instrument = instrument;
        const user: IUser = { id: 78181 };
        dailyAnalysisPost.user = user;

        activatedRoute.data = of({ dailyAnalysisPost });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dailyAnalysisPost));
        expect(comp.instrumentsSharedCollection).toContain(instrument);
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DailyAnalysisPost>>();
        const dailyAnalysisPost = { id: 123 };
        jest.spyOn(dailyAnalysisPostService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dailyAnalysisPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dailyAnalysisPost }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dailyAnalysisPostService.update).toHaveBeenCalledWith(dailyAnalysisPost);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DailyAnalysisPost>>();
        const dailyAnalysisPost = new DailyAnalysisPost();
        jest.spyOn(dailyAnalysisPostService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dailyAnalysisPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dailyAnalysisPost }));
        saveSubject.complete();

        // THEN
        expect(dailyAnalysisPostService.create).toHaveBeenCalledWith(dailyAnalysisPost);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DailyAnalysisPost>>();
        const dailyAnalysisPost = { id: 123 };
        jest.spyOn(dailyAnalysisPostService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dailyAnalysisPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dailyAnalysisPostService.update).toHaveBeenCalledWith(dailyAnalysisPost);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackInstrumentById', () => {
        it('Should return tracked Instrument primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInstrumentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
