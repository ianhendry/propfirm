import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

import { UIChart } from 'primeng/chart';

import { ISiteAccount } from '../../entities/site-account/site-account.model';
import { IMt4Trade } from '../../entities/mt-4-trade/mt-4-trade.model';
import { IMt4Account } from '../../entities/mt-4-account/mt-4-account.model';
import { ITradeChallenge } from '../../entities/trade-challenge/trade-challenge.model';
import { IAccountDataTimeSeries } from '../../entities/account-data-time-series/account-data-time-series.model'
import { IUser } from '../../entities/user/user.model';
import { SiteAccountService } from '../../entities/site-account/service/site-account.service';
import { TradeChallengeService } from '../../entities/trade-challenge/service/trade-challenge.service';
import { Mt4TradeService } from '../../entities/mt-4-trade/service/mt-4-trade.service';
import { AccountDataTimeSeriesService } from '../../entities/account-data-time-series/service/account-data-time-series.service'
import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from 'app/admin/user-management/service/user-management.service';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-userdashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
	@ViewChild("chart") chart: UIChart | undefined;
	tradesData: any;
	chartMt4Trades: any;
	timeSeriesData: any;
	
	siteAccounts?: ISiteAccount[] | null = null;
  	mt4Trades?: IMt4Trade[] | null = null;
	accountDataTimeSeries?: IAccountDataTimeSeries[];
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
		protected accountDataTimeSeriesService: AccountDataTimeSeriesService,
		private accountService: AccountService,
    	private userService: UserManagementService,
	) {
		this.tradesData = {
	      labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
	      datasets: [
	          {
	              label: 'Balance',
	              data: [65, 59, 80, 81, 56, 55, 40]
	          },
	          {
	              label: 'Equity',
	              data: [28, 48, 40, 19, 86, 27, 90]
	          }
	      ]
	    };
		this.chartMt4Trades = {
	      labels: [],
	      datasets: [
	          {
	              label: 'Profit',
	              data: [],
				  fill: true,
                  borderColor: '#42A5F5'

	          },
			  {
	              label: 'Size',
	              data: [],
				  fill: true,	
                  borderColor: '#FFA726'
	          }
	      ]
	    };
		this.timeSeriesData = {
	      labels: [],
	      datasets: [
	          {
	              label: 'Balance',
	              data: [],
				  fill: false,
                  borderColor: '#42A5F5'

	          },
			  {
	              label: 'Equity',
	              data: [],
				  fill: false,	
                  borderColor: '#FFA726'
	          }
	      ]
	    };
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
									this.showChallenge(this.tradeChallenges[0]);
									if (this.tradeChallenges[0].mt4Account) {
										this.getMt4Trades(this.tradeChallenges[0].mt4Account);
										this.getMt4AccountDataTimeSeries(this.tradeChallenges[0].mt4Account);
									}
								}
		                    } 
				    	);
					}
				}
			}
		}
	}
	
	getMt4Trades(mt4AccountId: IMt4Account):void {
		const criteria: { key: string; value: any; }[] = [];
	  	criteria.push({key: 'mt4AccountId.equals', value: mt4AccountId.id});

		if (mt4AccountId.id) {
			this.mt4TradeService.query({
		        page: 0,
		        size: 200,
		        sort: ['closeTime' + ',' + 'desc'],
	        	criteria
		      }).subscribe(
				(res: HttpResponse<IMt4Trade[]>) => {
					if (res.body) {
						this.mt4Trades = res.body;
						for (const mt4Trade of this.mt4Trades.reverse()) {
							this.chartMt4Trades.datasets[0].data = this.mt4Trades.map(a =>  a.profit);
							this.chartMt4Trades.datasets[1].data = this.mt4Trades.map(a =>  a.positionSize);
							this.chartMt4Trades.labels = this.mt4Trades.map(a => a.closeTime!.format('DD/MM/YYYY HH:mm:ss'));
	        				this.chart!.refresh();
						}
					}
	            } 
	    	);
		}
	}
	
	getMt4AccountDataTimeSeries(mt4AccountId: IMt4Account):void {
		const criteria: { key: string; value: any; }[] = [];
	  	criteria.push({key: 'mt4AccountId.equals', value: mt4AccountId.id});

		if (mt4AccountId.id) {
			this.accountDataTimeSeriesService.query({
		        page: 0,
		        size: 200,
		        sort: ['dateStamp' + ',' + 'desc'],
	        	criteria
		      }).subscribe(
				(res: HttpResponse<IAccountDataTimeSeries[]>) => {
					if (res.body) {
						this.accountDataTimeSeries = res.body;
						for (const accountTimeSeries of this.accountDataTimeSeries.reverse()) {
							this.timeSeriesData.datasets[0].data = this.accountDataTimeSeries.map(a =>  a.accountBalance);
							this.timeSeriesData.datasets[1].data = this.accountDataTimeSeries.map(a =>  a.accountEquity);
							this.timeSeriesData.labels = this.accountDataTimeSeries.map(a => a.dateStamp!.format('DD/MM/YYYY HH:mm:ss'));
	        				this.chart!.refresh();
						}
					}
	            } 
	    	);
		}
		this.isLoading = false;
	}
	
	showChallenge(challengeId: ITradeChallenge):void {
		if (challengeId.id) {
			this.tradeChallengeService.find(challengeId.id).subscribe(
				(res: HttpResponse<ITradeChallenge>) => {
					if (res.body) {
						this.tradeChallenge = res.body;
						if (this.tradeChallenge.mt4Account) {
							this.getMt4Trades(this.tradeChallenge.mt4Account);
						}
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
