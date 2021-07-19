import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AccountDataTimeSeriesService } from '../service/account-data-time-series.service';

import { AccountDataTimeSeriesComponent } from './account-data-time-series.component';

describe('Component Tests', () => {
  describe('AccountDataTimeSeries Management Component', () => {
    let comp: AccountDataTimeSeriesComponent;
    let fixture: ComponentFixture<AccountDataTimeSeriesComponent>;
    let service: AccountDataTimeSeriesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AccountDataTimeSeriesComponent],
      })
        .overrideTemplate(AccountDataTimeSeriesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AccountDataTimeSeriesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AccountDataTimeSeriesService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.accountDataTimeSeries?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
