package iai.glsib.backend.services;


import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import iai.glsib.backend.dtos.AccountOperationDTO;
import iai.glsib.backend.dtos.BankAccountDTO;
import iai.glsib.backend.dtos.BankAccountsDTO;
import iai.glsib.backend.dtos.CurrentBankAccountDTO;
import iai.glsib.backend.dtos.SavingBankAccountDTO;
import iai.glsib.backend.entities.AccountOperation;
import iai.glsib.backend.entities.BankAccount;
import iai.glsib.backend.entities.CurrentAccount;
import iai.glsib.backend.entities.Customer;
import iai.glsib.backend.entities.SavingAccount;
import iai.glsib.backend.enums.AccountStatus;
import iai.glsib.backend.enums.OperationType;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.exceptions.CustomerNotFoundException;
import iai.glsib.backend.mappers.BankAccountMapperImpl;
import iai.glsib.backend.repositories.AccountOperationRepository;
import iai.glsib.backend.repositories.BankAccountRepository;
import iai.glsib.backend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountService implements IBankAccountService<BankAccountDTO> {

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
    public BankAccountDTO getBankAccount(@NonNull String accountId) throws BankAccountNotFoundException {
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
    public void debit(@NonNull String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException {
        // Vérifiez que accountId n'est pas null
        Assert.notNull(accountId, "The accountId must not be null");

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
    public void credit(@NonNull String accountId, double amount, String desc) throws BankAccountNotFoundException {
        Assert.notNull(accountId, "The accountId must not be null");

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
    public void transfer(String accountIdSrc, String accountIdDst, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException {
         // Vérifiez que accountIdSrc et accountIdDst ne sont pas null
        Assert.notNull(accountIdSrc, "The source accountId must not be null");
        Assert.notNull(accountIdDst, "The destination accountId must not be null");
        AccountOperationDTO accountSourceOperationDTO = new AccountOperationDTO();
        accountSourceOperationDTO.setAccountId(accountIdSrc);
        accountSourceOperationDTO.setAmount(amount);
        debit(accountIdSrc , amount, desc);
        AccountOperationDTO accountDestOperationDTO = new AccountOperationDTO();
        accountDestOperationDTO.setAccountId(accountIdDst);
        accountDestOperationDTO.setAmount(amount);
        credit(accountIdDst , amount, desc);
    }

    private <T extends BankAccount> T createAccount(double initialBalance, AccountStatus status, @NonNull Long customerId, Supplier<T> accountSupplier) throws CustomerNotFoundException {
        log.info("Creating bank account for customer {}", customerId);
    
        Customer foundCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    
        T account = accountSupplier.get();
        account.setId(UUID.randomUUID().toString());
        account.setCreatedAt(new Date());
        account.setBalance(initialBalance);
        account.setStatus(status);
        account.setCustomer(foundCustomer);
    
        return bankAccountRepository.save(account);
    }
    
    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(
        double initialBalance, 
        double overdraft,
        @NonNull Long customerId
        ) throws CustomerNotFoundException {
        CurrentAccount currentAccount = createAccount(initialBalance, AccountStatus.CREATED, customerId, CurrentAccount::new);
        currentAccount.setOverDraft(overdraft);
        return dtoMapper.fromCurrentAccount(currentAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(
        double initialBalance, 
        double interestRate,
        @NonNull Long customerId
        ) throws CustomerNotFoundException {
        SavingAccount savingAccount = createAccount(initialBalance, AccountStatus.CREATED, customerId, SavingAccount::new);
        savingAccount.setInterestRate(interestRate);
        return dtoMapper.fromSavingAccount(savingAccount);
    }
    

   /*  @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overdraft, @NonNull Long customerid)
            throws CustomerNotFoundException {
        log.info("create current bank account");

        Customer customer = customerRepository.findById(customerid).orElse(null);

        if (customer == null) throw new CustomerNotFoundException("Customer not found");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overdraft);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCustomer(customer);
        CurrentAccount saveBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentAccount(saveBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, @NonNull Long customerid)
            throws CustomerNotFoundException {
        log.info("create saving bank account");

        Customer customer = customerRepository.findById(customerid).orElse(null);

        if (customer == null) throw new CustomerNotFoundException("Customer not found");

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCustomer(customer);
        SavingAccount saveBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingAccount(saveBankAccount);
    } */

    @Override
    public List<BankAccountDTO> listbankAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOs = bankAccounts.stream().map((bkAcc) -> {
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

    @Override
    public List<BankAccountDTO> bankAccountListOfCustomer(Long id) {
        Customer customer = new Customer();
        customer.setId(id);
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomer(customer);
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public BankAccountsDTO getBankAccountList(int page) {

        Page<BankAccount> bankAccounts = bankAccountRepository.findAll(PageRequest.of(page,5));
        List<BankAccountDTO> bankAccountDTOList = bankAccounts.stream().map(bankAccount -> {
            if(bankAccount instanceof  SavingAccount){
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            }else{
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        BankAccountsDTO  bankAccountsDTO = new BankAccountsDTO();
        bankAccountsDTO.setBankAccountDTOS(bankAccountDTOList);
        bankAccountsDTO.setTotalPage(bankAccounts.getTotalPages());
        return bankAccountsDTO;
    }

    @Override
    public BankAccountDTO updateBankAccount(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount;
        if(bankAccountDTO.getType().equals("saving account")) {
            SavingBankAccountDTO saving = new SavingBankAccountDTO();

            BeanUtils.copyProperties(bankAccountDTO,saving);
            bankAccount = dtoMapper.fromSavingBankAccountDTO(saving);
            bankAccount = bankAccountRepository.save(bankAccount);
            return dtoMapper.fromSavingAccount((SavingAccount) bankAccount);

        } else {
            CurrentBankAccountDTO current = new CurrentBankAccountDTO();

            BeanUtils.copyProperties(bankAccountDTO,current);
            bankAccount = dtoMapper.fromCurrentBankAccountDTO(current);
            bankAccount = bankAccountRepository.save(bankAccount);
            return dtoMapper.fromCurrentAccount((CurrentAccount) bankAccount);
        }
    }
}
