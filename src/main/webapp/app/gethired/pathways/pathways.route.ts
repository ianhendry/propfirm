import { Route } from '@angular/router';

import { PathwaysComponent } from './pathways.component';

export const PATHWAY_ROUTE: Route = {
  path: '',
  component: PathwaysComponent,
  data: {
    pageTitle: 'pathways.title',
  },
};
