import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AddressDetailsComponent } from './list/address-details.component';
import { AddressDetailsDetailComponent } from './detail/address-details-detail.component';
import { AddressDetailsUpdateComponent } from './update/address-details-update.component';
import { AddressDetailsDeleteDialogComponent } from './delete/address-details-delete-dialog.component';
import { AddressDetailsRoutingModule } from './route/address-details-routing.module';

@NgModule({
  imports: [SharedModule, AddressDetailsRoutingModule],
  declarations: [
    AddressDetailsComponent,
    AddressDetailsDetailComponent,
    AddressDetailsUpdateComponent,
    AddressDetailsDeleteDialogComponent,
  ],
  entryComponents: [AddressDetailsDeleteDialogComponent],
})
export class AddressDetailsModule {}
