import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InstrumentComponent } from './list/instrument.component';
import { InstrumentDetailComponent } from './detail/instrument-detail.component';
import { InstrumentUpdateComponent } from './update/instrument-update.component';
import { InstrumentDeleteDialogComponent } from './delete/instrument-delete-dialog.component';
import { InstrumentRoutingModule } from './route/instrument-routing.module';

@NgModule({
  imports: [SharedModule, InstrumentRoutingModule],
  declarations: [InstrumentComponent, InstrumentDetailComponent, InstrumentUpdateComponent, InstrumentDeleteDialogComponent],
  entryComponents: [InstrumentDeleteDialogComponent],
})
export class InstrumentModule {}
