import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ButtonModule } from 'primeng/button'; 
import { ChartModule } from 'primeng/chart';
import { ListboxModule } from 'primeng/listbox'

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE]), BrowserAnimationsModule, ButtonModule, ChartModule, ListboxModule],
  declarations: [HomeComponent],
})
export class HomeModule {}
