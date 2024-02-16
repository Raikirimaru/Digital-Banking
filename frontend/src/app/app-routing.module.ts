import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountsComponent } from './accounts/accounts.component';
import { AdminTemplateComponent } from './admin-template/admin-template.component';
import { CustomerAccountsComponent } from './customer-accounts/customer-accounts.component';
import { CustomersComponent } from './customers/customers.component';
import { authenticationGuard } from './guards/authentication.guard';
import { authorizationGuard } from './guards/authorization.guard';
import { HomeComponent } from './home/home.component';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { OneAccountComponent } from './one-account/one-account.component';
import { SigninComponent } from './signin/signin.component';
import { UpdateCustomerComponent } from './update-customer/update-customer.component';

const routes: Routes = [
  
  { path: "signin", component: SigninComponent },
  { path: "", redirectTo: "/signin", pathMatch: "full"},
  { path: "admin", component: AdminTemplateComponent, canActivate: [authenticationGuard], children: [
    { path: "home", component: HomeComponent },
    { path: "customers", component: CustomersComponent },
    { path: "accounts", component: AccountsComponent },
    { path: "accounts/:id", component: AccountsComponent },
    { path: "new-customer", component: NewCustomerComponent, canActivate : [authorizationGuard], data : { role : "ADMIN" } },
    { path: "customer-accounts/:id", component: CustomerAccountsComponent },
    { path: "not-authorized", component: NotAuthorizedComponent },
    { path: "update-customer/:id", component: UpdateCustomerComponent },
    { path: "one-account/:id", component: OneAccountComponent }
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
