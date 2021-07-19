import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { DashboardService } from './dashboard.service';

@Component({
  selector: 'jhi-register',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {

  constructor(private translateService: TranslateService, private registerService: DashboardService) {

  }

}
