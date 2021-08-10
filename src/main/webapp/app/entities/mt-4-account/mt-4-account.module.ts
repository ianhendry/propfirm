import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { Mt4AccountComponent } from './list/mt-4-account.component';
import { Mt4AccountDetailComponent } from './detail/mt-4-account-detail.component';
import { Mt4AccountUpdateComponent } from './update/mt-4-account-update.component';
import { Mt4AccountDeleteDialogComponent } from './delete/mt-4-account-delete-dialog.component';
import { Mt4AccountRoutingModule } from './route/mt-4-account-routing.module';

@NgModule({
  imports: [SharedModule, Mt4AccountRoutingModule],
  declarations: [Mt4AccountComponent, Mt4AccountDetailComponent, Mt4AccountUpdateComponent, Mt4AccountDeleteDialogComponent],
  entryComponents: [Mt4AccountDeleteDialogComponent],
})
export class Mt4AccountModule {}
