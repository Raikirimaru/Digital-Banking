import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Customer } from '../models/customer.model';
import { AuthService } from '../services/auth.service';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  customer!:Customer;

  constructor(public auth: AuthService,public customerService:CustomerService, public router: Router) {
  }

  ngOnInit(): void {
    // @ts-ignore

      this.customerService.getIdCustomerByname(localStorage.getItem('username')).subscribe({
        next: (data: any) => {
          this.customer =data;
        },
        error: (err: any) => {
          console.log(err);
        }
      });
    }

  handleSignOut() {
    this.auth.signout()
    this.router.navigateByUrl("/signin")
  }
}
