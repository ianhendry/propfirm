import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'site-account',
        data: { pageTitle: 'propfirmApp.siteAccount.home.title' },
        loadChildren: () => import('./site-account/site-account.module').then(m => m.SiteAccountModule),
      },
      {
        path: 'challenge-type',
        data: { pageTitle: 'propfirmApp.challengeType.home.title' },
        loadChildren: () => import('./challenge-type/challenge-type.module').then(m => m.ChallengeTypeModule),
      },
      {
        path: 'mt-4-account',
        data: { pageTitle: 'propfirmApp.mt4Account.home.title' },
        loadChildren: () => import('./mt-4-account/mt-4-account.module').then(m => m.Mt4AccountModule),
      },
      {
        path: 'address-details',
        data: { pageTitle: 'propfirmApp.addressDetails.home.title' },
        loadChildren: () => import('./address-details/address-details.module').then(m => m.AddressDetailsModule),
      },
      {
        path: 'trade-challenge',
        data: { pageTitle: 'propfirmApp.tradeChallenge.home.title' },
        loadChildren: () => import('./trade-challenge/trade-challenge.module').then(m => m.TradeChallengeModule),
      },
      {
        path: 'trade-journal-post',
        data: { pageTitle: 'propfirmApp.tradeJournalPost.home.title' },
        loadChildren: () => import('./trade-journal-post/trade-journal-post.module').then(m => m.TradeJournalPostModule),
      },
      {
        path: 'instrument',
        data: { pageTitle: 'propfirmApp.instrument.home.title' },
        loadChildren: () => import('./instrument/instrument.module').then(m => m.InstrumentModule),
      },
      {
        path: 'daily-analysis-post',
        data: { pageTitle: 'propfirmApp.dailyAnalysisPost.home.title' },
        loadChildren: () => import('./daily-analysis-post/daily-analysis-post.module').then(m => m.DailyAnalysisPostModule),
      },
      {
        path: 'user-comment',
        data: { pageTitle: 'propfirmApp.userComment.home.title' },
        loadChildren: () => import('./user-comment/user-comment.module').then(m => m.UserCommentModule),
      },
      {
        path: 'mt-4-trade',
        data: { pageTitle: 'propfirmApp.mt4Trade.home.title' },
        loadChildren: () => import('./mt-4-trade/mt-4-trade.module').then(m => m.Mt4TradeModule),
      },
      {
        path: 'account-data-time-series',
        data: { pageTitle: 'propfirmApp.accountDataTimeSeries.home.title' },
        loadChildren: () => import('./account-data-time-series/account-data-time-series.module').then(m => m.AccountDataTimeSeriesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
