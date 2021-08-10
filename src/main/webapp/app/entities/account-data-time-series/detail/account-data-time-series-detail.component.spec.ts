import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountDataTimeSeriesDetailComponent } from './account-data-time-series-detail.component';

describe('Component Tests', () => {
  describe('AccountDataTimeSeries Management Detail Component', () => {
    let comp: AccountDataTimeSeriesDetailComponent;
    let fixture: ComponentFixture<AccountDataTimeSeriesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AccountDataTimeSeriesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ accountDataTimeSeries: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AccountDataTimeSeriesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AccountDataTimeSeriesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load accountDataTimeSeries on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.accountDataTimeSeries).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
