import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { UserCommentDetailComponent } from './user-comment-detail.component';

describe('Component Tests', () => {
  describe('UserComment Management Detail Component', () => {
    let comp: UserCommentDetailComponent;
    let fixture: ComponentFixture<UserCommentDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UserCommentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ userComment: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UserCommentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserCommentDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
      jest.spyOn(window, 'open').mockImplementation(() => null);
    });

    describe('OnInit', () => {
      it('Should load userComment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userComment).toEqual(expect.objectContaining({ id: 123 }));
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
