import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pathways',
        data: { pageTitle: 'propfirmApp.siteAccount.home.title' },
        loadChildren: () => import('./pathways/pathways.module').then(m => m.PathwaysModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GethiredRoutingModule {}
