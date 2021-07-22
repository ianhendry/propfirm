import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { ISiteAccount } from '../../entities/site-account/site-account.model';
import { IMt4Trade } from '../../entities/mt-4-trade/mt-4-trade.model';
import { IMt4Account } from '../../entities/mt-4-account/mt-4-account.model';
import { ITradeChallenge } from '../../entities/trade-challenge/trade-challenge.model';
import { IUser } from '../../entities/user/user.model';
import { SiteAccountService } from '../../entities/site-account/service/site-account.service';
import { TradeChallengeService } from '../../entities/trade-challenge/service/trade-challenge.service';
import { Mt4TradeService } from '../../entities/mt-4-trade/service/mt-4-trade.service';
import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from 'app/admin/user-management/service/user-management.service';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-userdashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
	siteAccounts?: ISiteAccount[] | null = null;
  	mt4Trades?: IMt4Trade[] | null = null;
	tradeChallenges: ITradeChallenge[] = [];
	tradeChallenge?: ITradeChallenge;

  	isLoading = false;
	user: IUser | null = null;
	
	// data: any;
	
	constructor(
		protected dataUtils: DataUtils, 
		protected siteAccountService: SiteAccountService,
		protected tradeChallengeService: TradeChallengeService,
		protected mt4TradeService: Mt4TradeService,
		private accountService: AccountService,
    	private userService: UserManagementService
	) {
	}

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
	          	// this.isLoading = false;
				if (res.body) {
					for (const item of res.body) {
						this.siteAccounts = res.body;
						if (item.tradeChallenges) {
				        	this.getTradeChallenges(res.body)
						}
				    }      
				}
	        },
	        () => {
	          this.isLoading = false;
	          this.onError();
	        }
	     );
	}

	getTradeChallenges(data: ISiteAccount[]): void {
		for (const item of data) {
			if (item.tradeChallenges) {
				for (const challenge of item.tradeChallenges) {
					if (challenge.id) {
						this.tradeChallengeService.find(challenge.id).subscribe(
							(res: HttpResponse<ITradeChallenge>) => {
								if (res.body) {
									this.tradeChallenges.push(res.body)
								}
		                    } 
				    	);
					}
				}
			}
		}
		this.isLoading = false;
	}
	
	getMt4Trades(mt4AccountId: IMt4Account):void {
		const criteria: { key: string; value: any; }[] = [];
	  	criteria.push({key: 'mt4AccountId.equals', value: mt4AccountId.id});

		if (mt4AccountId.id) {
			this.mt4TradeService.query({
		        page: 0,
		        size: 20,
		        sort: [],
	        	criteria
		      }).subscribe(
				(res: HttpResponse<IMt4Trade[]>) => {
					if (res.body) {
						this.mt4Trades = res.body
					}
	            } 
	    	);
		}
	}
	
	
	showChallenge(challengeId: ITradeChallenge):void {
		if (challengeId.id) {
			this.tradeChallengeService.find(challengeId.id).subscribe(
				(res: HttpResponse<ITradeChallenge>) => {
					if (res.body) {
						this.tradeChallenge = res.body
					}
	            } 
	    	);
		}
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
