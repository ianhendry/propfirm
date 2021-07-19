import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TradeChallengeDetailComponent } from './trade-challenge-detail.component';

describe('Component Tests', () => {
  describe('TradeChallenge Management Detail Component', () => {
    let comp: TradeChallengeDetailComponent;
    let fixture: ComponentFixture<TradeChallengeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TradeChallengeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tradeChallenge: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TradeChallengeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TradeChallengeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tradeChallenge on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tradeChallenge).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
