package iai.glsib.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iai.glsib.backend.dtos.AccountHistoryDTO;
import iai.glsib.backend.dtos.AccountOperationDTO;
import iai.glsib.backend.entities.AccountOperation;
import iai.glsib.backend.entities.BankAccount;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.mappers.BankAccountMapperImpl;
import iai.glsib.backend.repositories.AccountOperationRepository;
import iai.glsib.backend.repositories.BankAccountRepository;
import lombok.AllArgsConstructor;
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
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new BankAccountNotFoundException("Account not found");
        Page<AccountOperation> accountOperation = accountOperationRepository.findByBankAccount_IdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationCollect = accountOperation.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOs(accountOperationCollect);
        accountHistoryDTO.setAccounId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPage(accountOperation.getTotalPages());
        return accountHistoryDTO;
    }

}
