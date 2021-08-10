import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChallengeTypeComponent } from './list/challenge-type.component';
import { ChallengeTypeDetailComponent } from './detail/challenge-type-detail.component';
import { ChallengeTypeUpdateComponent } from './update/challenge-type-update.component';
import { ChallengeTypeDeleteDialogComponent } from './delete/challenge-type-delete-dialog.component';
import { ChallengeTypeRoutingModule } from './route/challenge-type-routing.module';

@NgModule({
  imports: [SharedModule, ChallengeTypeRoutingModule],
  declarations: [ChallengeTypeComponent, ChallengeTypeDetailComponent, ChallengeTypeUpdateComponent, ChallengeTypeDeleteDialogComponent],
  entryComponents: [ChallengeTypeDeleteDialogComponent],
})
export class ChallengeTypeModule {}
