import { Customer } from './customer.model';
import { accountOperationDTOList } from './transaction.model';

export interface AccountDetails {
    accountId:               string;
    balance:                 number;
    currentPage:             number;
    totalPages:              number;
    size:                number;
    accountOperationDTOs: accountOperationDTOList[];
}

export interface BankAccountDTO {
    id: string;
    balance: number;
    createdAt?: any;
    status?: any;
    customerDTO: Customer;
    overDraft: number;
    type: string;
    interestRate: number;
}

export interface BankAccountDTOS {
    bankAccountDTOS: BankAccountDTO[];
    totalPage: number;
}


export interface savingAccount {
    id : string;
    balance : number;
    createdAt : Date;
    status : AccountStatus;
    customerDto : Customer;
    interestRate  : number;
}

export interface currentAccount {
    id : string;
    balance : number;
    createdAt : Date;
    status : AccountStatus;
    customerDto : Customer;
    overdraft  : number;
}

export enum AccountStatus {
    ACTIVATED,
    CREATED,
    SUSPENDED
}