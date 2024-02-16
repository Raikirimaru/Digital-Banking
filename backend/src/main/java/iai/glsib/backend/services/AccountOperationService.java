package iai.glsib.backend.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import iai.glsib.backend.dtos.AccountHistoryDTO;
import iai.glsib.backend.dtos.AccountOperationDTO;
import iai.glsib.backend.entities.AccountOperation;
import iai.glsib.backend.entities.BankAccount;
import iai.glsib.backend.enums.OperationType;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.mappers.BankAccountMapperImpl;
import iai.glsib.backend.repositories.AccountOperationRepository;
import iai.glsib.backend.repositories.BankAccountRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AccountOperationService implements IAccountOperationService {
    
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public List<AccountOperationDTO> accountHistory(String accountID) {
        log.info("list of history for the transactions");
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccount_Id(accountID);
        List<AccountOperationDTO> accountOperationDTOs = accountOperations.stream().map(accOps -> dtoMapper.fromAccountOperation(accOps)).collect(Collectors.toList());
        return accountOperationDTOs;
    }

    @Override
    public AccountHistoryDTO getAccountHistory(@NonNull String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);

        if (bankAccount == null) throw new BankAccountNotFoundException("Account not found");

        Page<AccountOperation> accountOperation = accountOperationRepository.findByBankAccount_IdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationCollect = accountOperation.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
       
        accountHistoryDTO.setAccountOperationDTOs(accountOperationCollect);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPage(accountOperation.getTotalPages());
        return accountHistoryDTO;
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
}
