import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  
}
