jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UserCommentService } from '../service/user-comment.service';
import { IUserComment, UserComment } from '../user-comment.model';
import { ITradeJournalPost } from 'app/entities/trade-journal-post/trade-journal-post.model';
import { TradeJournalPostService } from 'app/entities/trade-journal-post/service/trade-journal-post.service';
import { IDailyAnalysisPost } from 'app/entities/daily-analysis-post/daily-analysis-post.model';
import { DailyAnalysisPostService } from 'app/entities/daily-analysis-post/service/daily-analysis-post.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UserCommentUpdateComponent } from './user-comment-update.component';

describe('Component Tests', () => {
  describe('UserComment Management Update Component', () => {
    let comp: UserCommentUpdateComponent;
    let fixture: ComponentFixture<UserCommentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let userCommentService: UserCommentService;
    let tradeJournalPostService: TradeJournalPostService;
    let dailyAnalysisPostService: DailyAnalysisPostService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserCommentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UserCommentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserCommentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      userCommentService = TestBed.inject(UserCommentService);
      tradeJournalPostService = TestBed.inject(TradeJournalPostService);
      dailyAnalysisPostService = TestBed.inject(DailyAnalysisPostService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call TradeJournalPost query and add missing value', () => {
        const userComment: IUserComment = { id: 456 };
        const tradeJournalPost: ITradeJournalPost = { id: 74224 };
        userComment.tradeJournalPost = tradeJournalPost;

        const tradeJournalPostCollection: ITradeJournalPost[] = [{ id: 95294 }];
        jest.spyOn(tradeJournalPostService, 'query').mockReturnValue(of(new HttpResponse({ body: tradeJournalPostCollection })));
        const additionalTradeJournalPosts = [tradeJournalPost];
        const expectedCollection: ITradeJournalPost[] = [...additionalTradeJournalPosts, ...tradeJournalPostCollection];
        jest.spyOn(tradeJournalPostService, 'addTradeJournalPostToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userComment });
        comp.ngOnInit();

        expect(tradeJournalPostService.query).toHaveBeenCalled();
        expect(tradeJournalPostService.addTradeJournalPostToCollectionIfMissing).toHaveBeenCalledWith(
          tradeJournalPostCollection,
          ...additionalTradeJournalPosts
        );
        expect(comp.tradeJournalPostsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DailyAnalysisPost query and add missing value', () => {
        const userComment: IUserComment = { id: 456 };
        const dailyAnalysisPost: IDailyAnalysisPost = { id: 65749 };
        userComment.dailyAnalysisPost = dailyAnalysisPost;

        const dailyAnalysisPostCollection: IDailyAnalysisPost[] = [{ id: 31546 }];
        jest.spyOn(dailyAnalysisPostService, 'query').mockReturnValue(of(new HttpResponse({ body: dailyAnalysisPostCollection })));
        const additionalDailyAnalysisPosts = [dailyAnalysisPost];
        const expectedCollection: IDailyAnalysisPost[] = [...additionalDailyAnalysisPosts, ...dailyAnalysisPostCollection];
        jest.spyOn(dailyAnalysisPostService, 'addDailyAnalysisPostToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userComment });
        comp.ngOnInit();

        expect(dailyAnalysisPostService.query).toHaveBeenCalled();
        expect(dailyAnalysisPostService.addDailyAnalysisPostToCollectionIfMissing).toHaveBeenCalledWith(
          dailyAnalysisPostCollection,
          ...additionalDailyAnalysisPosts
        );
        expect(comp.dailyAnalysisPostsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const userComment: IUserComment = { id: 456 };
        const user: IUser = { id: 62690 };
        userComment.user = user;

        const userCollection: IUser[] = [{ id: 54306 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userComment });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const userComment: IUserComment = { id: 456 };
        const tradeJournalPost: ITradeJournalPost = { id: 7641 };
        userComment.tradeJournalPost = tradeJournalPost;
        const dailyAnalysisPost: IDailyAnalysisPost = { id: 18460 };
        userComment.dailyAnalysisPost = dailyAnalysisPost;
        const user: IUser = { id: 88448 };
        userComment.user = user;

        activatedRoute.data = of({ userComment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(userComment));
        expect(comp.tradeJournalPostsSharedCollection).toContain(tradeJournalPost);
        expect(comp.dailyAnalysisPostsSharedCollection).toContain(dailyAnalysisPost);
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserComment>>();
        const userComment = { id: 123 };
        jest.spyOn(userCommentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userComment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userComment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(userCommentService.update).toHaveBeenCalledWith(userComment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserComment>>();
        const userComment = new UserComment();
        jest.spyOn(userCommentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userComment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userComment }));
        saveSubject.complete();

        // THEN
        expect(userCommentService.create).toHaveBeenCalledWith(userComment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserComment>>();
        const userComment = { id: 123 };
        jest.spyOn(userCommentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userComment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(userCommentService.update).toHaveBeenCalledWith(userComment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTradeJournalPostById', () => {
        it('Should return tracked TradeJournalPost primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTradeJournalPostById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDailyAnalysisPostById', () => {
        it('Should return tracked DailyAnalysisPost primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDailyAnalysisPostById(0, entity);
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
