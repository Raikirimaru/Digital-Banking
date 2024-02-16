package iai.glsib.backend.services;

import java.util.List;

import iai.glsib.backend.dtos.AccountHistoryDTO;
import iai.glsib.backend.dtos.AccountOperationDTO;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import lombok.NonNull;

public interface IAccountOperationService {
    List<AccountOperationDTO> accountHistory(String accountID);
    
    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    void debit(@NonNull String accountId, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(@NonNull String accountId, double amount, String desc) throws BankAccountNotFoundException;

    void transfer(String accountIdSrc, String accountIdDst, double amount, String desc) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
