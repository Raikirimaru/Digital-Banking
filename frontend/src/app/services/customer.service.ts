import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BankAccountDTOS } from '../models/account.model';
import { Customer, CustomerDTOS } from '../models/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  baseUrl: string = 'http://localhost:8080'

  constructor(private http : HttpClient) { }

  public getCustomers() : Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(`${this.baseUrl}/customers`)
  }

  public getOneCustomer(id: number): any {
    return this.http.get<any>(`${this.baseUrl}/customers/${id}`);
  }

  public getIdCustomerByname(name: string | undefined): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/customers/name/${name}`);
  }

  public searchAccountByCustomer(page: number): Observable<BankAccountDTOS> {
    return this.http.get<BankAccountDTOS>(`${this.baseUrl}/accounts/searchAccount?page=${page}`)
  }

  public searchCustomers(keyword: string, page: number) : Observable<CustomerDTOS> {
    return this.http.get<CustomerDTOS>(`${this.baseUrl}/customers/search?keyword=${keyword}&page=${page}`)
  }

  public saveCustomer(customer: Customer) : Observable<Customer> {
    return this.http.post<Customer>(`${this.baseUrl}/customers`, customer)
  }

  public deleteCustomer(id : number) : Observable<void>  {
    return this.http.delete<void>(`${this.baseUrl}/customers/${id}`)
  }

  updateCustomer(customer: Customer): Observable<Array<Customer>> {
    return this.http.put<Array<Customer>>(`${this.baseUrl}/customers/${customer.id}`, customer);
  }

  getCustomerById(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.baseUrl}/customers/${id}`);
  }

  public getAccountsOfCustomer(id: number): any {
    return this.http.get<any>(`${this.baseUrl}/customers/${id}/accounts`);
  }
}
