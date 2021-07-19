import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-pathways',
  templateUrl: './pathways.component.html',
  styleUrls: ['./pathways.component.scss'],
})
export class PathwaysComponent   {
    
constructor(private router: Router) {}

	
  
	
}
