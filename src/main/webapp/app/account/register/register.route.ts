import { Route } from '@angular/router';

import { RegisterComponent } from './register.component';

export const registerRoute: Route = {
  path: 'register/:pathway',
  component: RegisterComponent,
  data: {
    pageTitle: 'register.title',
  },
};
