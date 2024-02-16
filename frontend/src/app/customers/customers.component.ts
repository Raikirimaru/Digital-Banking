import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, catchError, throwError } from 'rxjs';
import Swal from 'sweetalert2';
import { Customer, CustomerDTOS } from '../models/customer.model';
import { CustomerService } from './../services/customer.service';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.scss']
})
export class CustomersComponent {
  customers!: Observable<CustomerDTOS>;
  errorMessage: string | undefined;
  searchFormGroup!: FormGroup;
  currentPage: number = 0;
  totalPage!: number;
  pageSize: number = 5;
  customer!:Customer| undefined;
  username: string | undefined;
  roles!: string | null;

  constructor(private customerService: CustomerService, private fb: FormBuilder, private router: Router) {
  }

  ngOnInit(): void {
    this.roles = localStorage.getItem("ROLES");
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control("")
    })
    // @ts-ignore
    this.username=localStorage.getItem('username');

    this.getCustomer();
    this.searchCustomers();
  }

  searchCustomers() {
    let kw = this.searchFormGroup.value.keyword;
    this.customers = this.customerService.searchCustomers(kw, this.currentPage).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );

  }
  getCustomer()
  {
    if(this.username!="admin")
    {
      console.log("getIdCustomerByname")
      this.customerService.getIdCustomerByname(this.username).subscribe({
        next: (data: any) => {
          this.customer =data;
        },
        error: (err: any) => {
          console.log(err);
        }
      });
    }
  }

  handleDeleteButton(customer: Customer) {
    Swal.fire({
      title: 'Are you sure that you want to delete this customer ?',
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: 'Yes',
      denyButtonText: `No`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.customerService.deleteCustomer(customer.id).subscribe(
          {
            next: (resp) => {
              Swal.fire('Deleted successfully !', '', 'success')
              this.searchCustomers()
            },
            error: (err) => {
              console.log(err);
            }
          }
        );

      }
    });
  }

  handleUpdateButton(customer: Customer) {
    this.customerService.updateCustomer(customer)
    this.router.navigateByUrl("/admin/customers")
  }

  goToUpdateCustomer(customer: Customer) {
    this.customerService.updateCustomer(customer)
    this.router.navigateByUrl("/admin/update-customer/" + customer.id);
  }

  handleCustomerAccounts(customer: Customer) {
    this.router.navigateByUrl("/admin/customer-accounts/" + customer.id, {state: customer})
  }

  gotoPage(page: number) {
    this.currentPage = page;
    this.searchCustomers();
  }
}
