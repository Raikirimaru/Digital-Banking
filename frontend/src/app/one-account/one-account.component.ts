import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, catchError, throwError } from 'rxjs';
import Swal from 'sweetalert2';
import { AccountDetails } from '../models/account.model';
import { AccountsService } from '../services/accounts.service';

@Component({
  selector: 'app-one-account',
  templateUrl: './one-account.component.html',
  styleUrls: ['./one-account.component.scss']
})
export class OneAccountComponent {
  accountFormGroup!: FormGroup;
  currentPage: number = 0;
  pageSize: number = 5;
  accountObservable!: Observable<AccountDetails>
  operationFromGroup!: FormGroup;
  errorMessage!: string;
  accountId: any;

  constructor(private route: ActivatedRoute, private fb: FormBuilder, private accountService: AccountsService) {
  }

  ngOnInit(): void {
    this.accountFormGroup = this.fb.group({
      accountId: this.fb.control('')
    });
    this.operationFromGroup = this.fb.group({
      operationType: this.fb.control(null),
      amount: this.fb.control(0),
      description: this.fb.control(null),
      accountDestination: this.fb.control(null)
    })

    this.searchAccount();

  }

  searchAccount() {
    this.accountId= this.route.snapshot.params['id'];
    this.accountObservable = this.accountService.getAccount(this.accountId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );
  }

  gotoPage(page: number) {
    this.currentPage = page;
    this.searchAccount();
  }

  handleAccountOperation() {
    let accountId: string = this.accountId;
    let operationType = this.operationFromGroup.value.operationType;
    let amount: number = this.operationFromGroup.value.amount;
    let description: string = this.operationFromGroup.value.description;
    let accountDestination: string = this.operationFromGroup.value.accountDestination;

    if(operationType=='DEBIT'){
      this.accountService.debit(accountId,amount,description).subscribe(
        {
          next: data => {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: "The debit operation has been successfully executed !",
              showConfirmButton: false,
              timer: 2500
            });
            this.searchAccount();
            this.accountFormGroup.reset();
          },
          error: err => {
          }
        }
      );
    }else if(operationType=='CREDIT'){
      this.accountService.credit(accountId,amount,description).subscribe(
        {
          next: data => {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: "The credit operation has been successfully executed !",
              showConfirmButton: false,
              timer: 2500
            });
            this.searchAccount();
            this.accountFormGroup.reset();
          },
          error: err => {
          }
        }
      );
    }else if(operationType=='TRANSFER'){
      this.accountService.transfer(accountDestination,accountId,amount,description).subscribe(
        {
          next: data => {
              Swal.fire({
                position: 'center',
                icon: 'success',
                title: "The transfer operation has been successfully executed !",
                showConfirmButton: false,
                timer: 2500
              });
            this.searchAccount();
            this.accountFormGroup.reset();
          },
          error: err => {
          }
        }
      );
    }
  }
}
