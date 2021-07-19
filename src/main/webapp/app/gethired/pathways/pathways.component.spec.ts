jest.mock('app/core/auth/account.service');
jest.mock('@angular/router');

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import { PathwaysComponent } from './pathways.component';

describe('Component Tests', () => {
  describe('Home Component', () => {
    let comp: PathwaysComponent;
    let fixture: ComponentFixture<PathwaysComponent>;
    let mockAccountService: AccountService;
    let mockRouter: Router;
    const account: Account = {
      activated: true,
      authorities: [],
      email: '',
      firstName: null,
      langKey: '',
      lastName: null,
      login: 'login',
      imageUrl: null,
    };

    beforeEach(
      waitForAsync(() => {
        TestBed.configureTestingModule({
          declarations: [PathwaysComponent],
          providers: [AccountService, Router],
        })
          .overrideTemplate(PathwaysComponent, '')
          .compileComponents();
      })
    );

    beforeEach(() => {
      fixture = TestBed.createComponent(PathwaysComponent);
      comp = fixture.componentInstance;
      mockAccountService = TestBed.inject(AccountService);
      mockAccountService.identity = jest.fn(() => of(null));
      mockAccountService.getAuthenticationState = jest.fn(() => of(null));
      mockRouter = TestBed.inject(Router);
    });

    
    

    
  });
});
