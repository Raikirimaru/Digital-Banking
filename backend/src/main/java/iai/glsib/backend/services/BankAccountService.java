package iai.glsib.backend.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iai.glsib.backend.dtos.BankAccountDTO;
import iai.glsib.backend.dtos.CurrentBankAccountDTO;
import iai.glsib.backend.dtos.SavingBankAccountDTO;
import iai.glsib.backend.entities.AccountOperation;
import iai.glsib.backend.entities.BankAccount;
import iai.glsib.backend.entities.CurrentAccount;
import iai.glsib.backend.entities.Customer;
import iai.glsib.backend.entities.SavingAccount;
import iai.glsib.backend.enums.OperationType;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.exceptions.CustomerNotFoundException;
import iai.glsib.backend.mappers.BankAccountMapperImpl;
import iai.glsib.backend.repositories.AccountOperationRepository;
import iai.glsib.backend.repositories.BankAccountRepository;
import iai.glsib.backend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountService implements IBankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    /* public BankAccountService(
        CustomerRepository customerRepository, 
        BankAccountRepository bankAccountRepository, 
        AccountOperationRepository accountOperationRepository
    ) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
    } */


    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                                                    .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        
        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                                                    .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        if (bankAccount.getBalance() < amount) {
            System.out.println("Balance not sufficient. Current balance: " + bankAccount.getBalance());
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(desc);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String desc) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                                                    .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(desc);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSrc, String accountIdDst, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSrc, amount, "Transfer to" + accountIdDst);
        credit(accountIdDst, amount, "Transfer from " + accountIdSrc);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, Long customerid)
            throws CustomerNotFoundException {
        log.info("create current bank account");

        Customer customer = customerRepository.findById(customerid).orElse(null);

        if (customer == null) throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overdraft);
        currentAccount.setCustomer(customer);
        CurrentAccount saveBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentAccount(saveBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerid)
            throws CustomerNotFoundException {
        log.info("create saving bank account");

        Customer customer = customerRepository.findById(customerid).orElse(null);

        if (customer == null) throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount saveBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingAccount(saveBankAccount);
    }

    @Override
    public List<BankAccountDTO> listbankAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOs = bankAccounts.stream().map(bkAcc -> {
            if (bkAcc instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bkAcc;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bkAcc;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOs;
    }
}
