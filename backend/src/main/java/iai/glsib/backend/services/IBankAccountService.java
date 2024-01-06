package iai.glsib.backend.services;

import java.util.List;

import iai.glsib.backend.dtos.AccountHistoryDTO;
import iai.glsib.backend.dtos.AccountOperationDTO;
import iai.glsib.backend.dtos.BankAccountDTO;
import iai.glsib.backend.dtos.CurrentBankAccountDTO;
import iai.glsib.backend.dtos.CustomerDTO;
import iai.glsib.backend.dtos.SavingBankAccountDTO;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.exceptions.CustomerNotFoundException;

public interface IBankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDto);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerid) throws CustomerNotFoundException;

    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerid) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();

    List<BankAccountDTO> listbankAccounts();

    List<AccountOperationDTO> accountHistory(String accountID);
    
    List<CustomerDTO> searchCustomers(String keyword);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDto);

    void deleteCustomer(Long customerId);

    // depôt
    void debit(String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;

    // retrait
    void credit(String accountId, double amount, String desc) throws BankAccountNotFoundException;

    // virements
    void transfer(String accountIdSrc, String accountIdDst, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
