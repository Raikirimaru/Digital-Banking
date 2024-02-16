package iai.glsib.backend.services;

import java.util.List;

import iai.glsib.backend.dtos.BankAccountDTO;
import iai.glsib.backend.dtos.BankAccountsDTO;
import iai.glsib.backend.dtos.CurrentBankAccountDTO;
import iai.glsib.backend.dtos.SavingBankAccountDTO;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.exceptions.CustomerNotFoundException;

public interface IBankAccountService {
        CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerid) throws CustomerNotFoundException;

    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerid) throws CustomerNotFoundException;

    List<BankAccountDTO> listbankAccounts();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    // depôt
    void debit(String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;

    // retrait
    void credit(String accountId, double amount, String desc) throws BankAccountNotFoundException;

    // virements
    void transfer(String accountIdSrc, String accountIdDst, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountListOfCustomer(Long id);

    BankAccountsDTO getBankAccountList(int page);

    BankAccountDTO updateBankAccount(BankAccountDTO bankAccountDTO);
}
