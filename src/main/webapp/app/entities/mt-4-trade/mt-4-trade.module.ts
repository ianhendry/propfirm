import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { Mt4TradeComponent } from './list/mt-4-trade.component';
import { Mt4TradeDetailComponent } from './detail/mt-4-trade-detail.component';
import { Mt4TradeUpdateComponent } from './update/mt-4-trade-update.component';
import { Mt4TradeDeleteDialogComponent } from './delete/mt-4-trade-delete-dialog.component';
import { Mt4TradeRoutingModule } from './route/mt-4-trade-routing.module';

@NgModule({
  imports: [SharedModule, Mt4TradeRoutingModule],
  declarations: [Mt4TradeComponent, Mt4TradeDetailComponent, Mt4TradeUpdateComponent, Mt4TradeDeleteDialogComponent],
  entryComponents: [Mt4TradeDeleteDialogComponent],
})
export class Mt4TradeModule {}
