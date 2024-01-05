package iai.glsib.backend.services;

import java.util.List;

import iai.glsib.backend.entities.BankAccount;
import iai.glsib.backend.entities.CurrentAccount;
import iai.glsib.backend.entities.Customer;
import iai.glsib.backend.entities.SavingAccount;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.exceptions.CustomerNotFoundException;

public interface IBankAccountService {
    Customer saveCustomer(Customer customer);

    CurrentAccount saveCurrentBankAccount(double initialBalance, double overdraft, Long customerid) throws CustomerNotFoundException;

    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerid) throws CustomerNotFoundException;

    List<Customer> listCustomers();

    List<BankAccount> listbankAccounts();

    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;

    // depôt
    void debit(String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;

    // retrait
    void credit(String accountId, double amount, String desc) throws BankAccountNotFoundException;

    // virements
    void transfer(String accountIdSrc, String accountIdDst, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
