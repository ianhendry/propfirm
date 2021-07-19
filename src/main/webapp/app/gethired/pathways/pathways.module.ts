import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PATHWAY_ROUTE } from './pathways.route';
import { PathwaysComponent } from './pathways.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([PATHWAY_ROUTE])],
  declarations: [PathwaysComponent],
})
export class PathwaysModule {}
