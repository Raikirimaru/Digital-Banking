import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountDetails, BankAccountDTO, currentAccount, savingAccount } from '../models/account.model';

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  baseUrl: string = 'http://localhost:8080'

  constructor(private http : HttpClient) { }

  public getAccount(accountID : string, page : number, size : number) : Observable<AccountDetails> {
    return this.http.get<AccountDetails>(`${this.baseUrl}/accounts/${accountID}/pageOperations?page=${page}&size=${size}`)
  }

  public debit(accountID : string, amount : number, desc : string) {
    let data = {
      accountId : accountID,
      amount : amount,
      description : desc
    }
    return this.http.post(`${this.baseUrl}/accounts/debit`, data)
  }

  public credit(accountID : string, amount : number, desc : string) {
    let data = {
      accountId : accountID,
      amount : amount,
      description : desc
    }
    return this.http.post(`${this.baseUrl}/accounts/credit`, data)
  }

  public transfer(
    accountSource : string,
    accountDestination : string,
    amount : number,
    desc : string
  ) : Observable<void> {
    const transferRequest = {
      accountSource : accountSource,
      accountDestination : accountDestination,
      amount : amount,
      description : desc
    };

    return this.http.post<void>(
      `${this.baseUrl}/accounts/transfer`,
      transferRequest
    );
  }

  public saving_account(initialBalance : number, interestRate : number, customerid : number) : Observable<savingAccount> {
    let options = {
      headers : new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    }
    
    let params = new HttpParams().set("initialBalance", initialBalance).set("interestRate", interestRate).set("customerId", customerid)

    return this.http.post<savingAccount>(`${this.baseUrl}/accounts/saving`, params, options) 
  }

  public current_account(initialBalance : number, overdraft : number, customerid : number) : Observable<currentAccount> {
    let options = {
      headers : new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    }

    let params = new HttpParams().set("initialBalance", initialBalance).set("overdraft", overdraft).set("customerId", customerid)

    return this.http.post<currentAccount>(`${this.baseUrl}/accounts/current`, params, options) 
  }

  updateAccount(bankAccount: BankAccountDTO):Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/accounts/${bankAccount.id}}`, bankAccount);
  }
}
