jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TradeJournalPostService } from '../service/trade-journal-post.service';
import { ITradeJournalPost, TradeJournalPost } from '../trade-journal-post.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TradeJournalPostUpdateComponent } from './trade-journal-post-update.component';

describe('Component Tests', () => {
  describe('TradeJournalPost Management Update Component', () => {
    let comp: TradeJournalPostUpdateComponent;
    let fixture: ComponentFixture<TradeJournalPostUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tradeJournalPostService: TradeJournalPostService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TradeJournalPostUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TradeJournalPostUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TradeJournalPostUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tradeJournalPostService = TestBed.inject(TradeJournalPostService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const tradeJournalPost: ITradeJournalPost = { id: 456 };
        const user: IUser = { id: 86397 };
        tradeJournalPost.user = user;

        const userCollection: IUser[] = [{ id: 72863 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tradeJournalPost });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tradeJournalPost: ITradeJournalPost = { id: 456 };
        const user: IUser = { id: 38012 };
        tradeJournalPost.user = user;

        activatedRoute.data = of({ tradeJournalPost });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tradeJournalPost));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TradeJournalPost>>();
        const tradeJournalPost = { id: 123 };
        jest.spyOn(tradeJournalPostService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeJournalPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tradeJournalPost }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tradeJournalPostService.update).toHaveBeenCalledWith(tradeJournalPost);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TradeJournalPost>>();
        const tradeJournalPost = new TradeJournalPost();
        jest.spyOn(tradeJournalPostService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeJournalPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tradeJournalPost }));
        saveSubject.complete();

        // THEN
        expect(tradeJournalPostService.create).toHaveBeenCalledWith(tradeJournalPost);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TradeJournalPost>>();
        const tradeJournalPost = { id: 123 };
        jest.spyOn(tradeJournalPostService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tradeJournalPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tradeJournalPostService.update).toHaveBeenCalledWith(tradeJournalPost);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
