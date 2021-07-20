import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { ISiteAccount } from '../../entities/site-account/site-account.model';
import { ITradeChallenge } from '../../entities/trade-challenge/trade-challenge.model';
import { IUser } from '../../entities/user/user.model';
import { SiteAccountService } from '../../entities/site-account/service/site-account.service';
import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from 'app/admin/user-management/service/user-management.service';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-userdashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
	siteAccounts?: ISiteAccount[] | null = null;
  	tradeChallenge: ITradeChallenge[] | null = null;
  	isLoading = false;
	user: IUser | null = null;
	
	data: any;
	
	constructor(
		protected dataUtils: DataUtils, 
		protected siteAccountService: SiteAccountService,
		private accountService: AccountService,
    	private userService: UserManagementService
	) {}


	 ngOnInit(): void {
		this.accountService.getAuthenticationState().subscribe(
		account => {
		   if (account){
		      this.userService.findOneUser(account.login).subscribe(
		         user => this.loadSiteAccount(user)
		       );
		   }
		}
		);
		this.data = {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
            datasets: [
                {
                    label: 'First Dataset',
                    data: [65, 59, 80, 81, 56, 55, 40]
                },
                {
                    label: 'Second Dataset',
                    data: [28, 48, 40, 19, 86, 27, 90]
                }
            ]
        }
  	}

	loadSiteAccount(user: IUser): void {
		this.user = user;
	    this.isLoading = true;
		const criteria: { key: string; value: any; }[] = [];
	  	criteria.push({key: 'userId.equals', value: user.id});
	
	    this.siteAccountService
	      .query({
	        page: 0,
	        size: 10,
	        sort: [],
	        criteria
	      })
	      .subscribe(
	        (res: HttpResponse<ISiteAccount[]>) => {
	          this.isLoading = false;
	          this.siteAccounts = res.body;
	        },
	        () => {
	          this.isLoading = false;
	          this.onError();
	        }
	      );
	  }

	onError(): void {
		alert("Error reading the site accounts");
	}
	
	byteSize(base64String: string): string {
	    return this.dataUtils.byteSize(base64String);
	}
	
	openFile(base64String: string, contentType: string | null | undefined): void {
	    this.dataUtils.openFile(base64String, contentType);
	}
	
	previousState(): void {
	    window.history.back();
	}

// to get this users challenges
// select * from site_account where user_id = 3;
// select * from trade_challenge where site_account = the above
}
