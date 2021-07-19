import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SiteAccountComponent } from './list/site-account.component';
import { SiteAccountDetailComponent } from './detail/site-account-detail.component';
import { SiteAccountUpdateComponent } from './update/site-account-update.component';
import { SiteAccountDeleteDialogComponent } from './delete/site-account-delete-dialog.component';
import { SiteAccountRoutingModule } from './route/site-account-routing.module';

@NgModule({
  imports: [SharedModule, SiteAccountRoutingModule],
  declarations: [SiteAccountComponent, SiteAccountDetailComponent, SiteAccountUpdateComponent, SiteAccountDeleteDialogComponent],
  entryComponents: [SiteAccountDeleteDialogComponent],
})
export class SiteAccountModule {}
