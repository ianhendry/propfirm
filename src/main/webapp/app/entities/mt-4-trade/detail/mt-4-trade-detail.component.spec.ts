import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Mt4TradeDetailComponent } from './mt-4-trade-detail.component';

describe('Component Tests', () => {
  describe('Mt4Trade Management Detail Component', () => {
    let comp: Mt4TradeDetailComponent;
    let fixture: ComponentFixture<Mt4TradeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [Mt4TradeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mt4Trade: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(Mt4TradeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Mt4TradeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mt4Trade on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mt4Trade).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
