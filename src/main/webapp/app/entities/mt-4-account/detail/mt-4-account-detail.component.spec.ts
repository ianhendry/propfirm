import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Mt4AccountDetailComponent } from './mt-4-account-detail.component';

describe('Component Tests', () => {
  describe('Mt4Account Management Detail Component', () => {
    let comp: Mt4AccountDetailComponent;
    let fixture: ComponentFixture<Mt4AccountDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [Mt4AccountDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mt4Account: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(Mt4AccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Mt4AccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mt4Account on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mt4Account).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
