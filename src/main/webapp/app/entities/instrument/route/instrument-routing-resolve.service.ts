import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInstrument, Instrument } from '../instrument.model';
import { InstrumentService } from '../service/instrument.service';

@Injectable({ providedIn: 'root' })
export class InstrumentRoutingResolveService implements Resolve<IInstrument> {
  constructor(protected service: InstrumentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInstrument> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((instrument: HttpResponse<Instrument>) => {
          if (instrument.body) {
            return of(instrument.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Instrument());
  }
}
