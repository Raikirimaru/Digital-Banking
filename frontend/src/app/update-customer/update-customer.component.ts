import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { Customer } from '../models/customer.model';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-update-customer',
  templateUrl: './update-customer.component.html',
  styleUrls: ['./update-customer.component.scss']
})
export class UpdateCustomerComponent {
  updateCustomerFormGroup!: FormGroup;

  constructor(private fb: FormBuilder, private serviceCustomer: CustomerService, private router: ActivatedRoute, private routerNav: Router) {
  }

  ngOnInit(): void {
    this.updateCustomerFormGroup = this.fb.group({
      name: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      email: this.fb.control(null, [Validators.email]),
      id: this.fb.control(null),
    })
    this.getCustomer();
  }

  updateCustomer() {
    let customer: Customer = this.updateCustomerFormGroup.value;
    customer.id = this.router.snapshot.params['id'];
    return this.serviceCustomer.updateCustomer(customer).subscribe(
      {
        next: data => {
          Swal.fire({
            position: 'center',
            icon: 'success',
            title: "The customer has been successfully updated !",
            showConfirmButton: false,
            timer: 1500
          });
          this.routerNav.navigateByUrl('/admin/customers')
        },
        error: err => {
          Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Something went wrong!",
            footer: '<a href="#">Why do I have this issue?</a>'
          });
        }
      }
    );
  }

  private getCustomer() {
    let customer: Customer;
    return this.serviceCustomer.getCustomerById(this.router.snapshot.params['id']).subscribe(
      {
        next: data => {
          console.log(data)
          customer = data
          console.log(customer)
          this.updateCustomerFormGroup.patchValue({
            email: customer.email,
            name: customer.name,
          })
        },
        error: err => {
          Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Something went wrong!",
            footer: '<a href="#">Why do I have this issue?</a>'
          });
        }
      }
    );
  }
}
