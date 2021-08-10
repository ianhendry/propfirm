import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserCommentComponent } from './list/user-comment.component';
import { UserCommentDetailComponent } from './detail/user-comment-detail.component';
import { UserCommentUpdateComponent } from './update/user-comment-update.component';
import { UserCommentDeleteDialogComponent } from './delete/user-comment-delete-dialog.component';
import { UserCommentRoutingModule } from './route/user-comment-routing.module';

@NgModule({
  imports: [SharedModule, UserCommentRoutingModule],
  declarations: [UserCommentComponent, UserCommentDetailComponent, UserCommentUpdateComponent, UserCommentDeleteDialogComponent],
  entryComponents: [UserCommentDeleteDialogComponent],
})
export class UserCommentModule {}
