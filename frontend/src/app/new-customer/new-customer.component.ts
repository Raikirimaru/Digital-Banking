import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { concatMap, of } from 'rxjs';
import Swal from 'sweetalert2';
import { Customer } from '../models/customer.model';
import { AccountsService } from '../services/accounts.service';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.scss']
})
export class NewCustomerComponent {
  newCustomerFormGroup!: FormGroup;

  constructor(private fb: FormBuilder, private serviceCustomer: CustomerService, private router: Router, private accountService : AccountsService) {
  }

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group({
      name: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      email: this.fb.control(null, [Validators.email]),
      DateOfBirth: this.fb.control(null, [Validators.required]),
      tel: this.fb.control(null, [Validators.required, Validators.minLength(8)]),
      type : this.fb.control(null),
      InitialBalance : this.fb.control(0),
      savingInterestRate : this.fb.control(0),
      currentOverdraftLimit : this.fb.control(0)
    })
  }

  addCustomer() {
    let customer: Customer = this.newCustomerFormGroup.value;
    let AccountType = this.newCustomerFormGroup.value.type;
    this.serviceCustomer.saveCustomer(customer).pipe(
      concatMap((customerData) => {
        let customerId = customerData.id;
        let initbalance = this.newCustomerFormGroup.value.InitialBalance;
        let interestRate = this.newCustomerFormGroup.value.savingInterestRate;
        let overdraft = this.newCustomerFormGroup.value.currentOverdraftLimit;
  
        switch (AccountType) {
          case 'CurrentAccount':
            return this.accountService.current_account(initbalance, overdraft, customerId);
            
          case 'SavingAccount':
            return this.accountService.saving_account(initbalance, interestRate, customerId);
            
          default:
            return of(null); // Observable of null if no specific case matches
        }
      })
    ).subscribe({
      next: () => {
        Swal.fire({
          title: "Good job!",
          text: "Customer and Account information saved successfully",
          icon: "success"
        });
        this.newCustomerFormGroup.reset();
        this.router.navigateByUrl('/admin/customers');
      },
      error: (e) => {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Something went wrong!",
        });
        console.log(e);
      }
    });
  }  
}

