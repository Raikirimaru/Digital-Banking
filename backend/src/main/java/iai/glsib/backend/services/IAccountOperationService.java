package iai.glsib.backend.services;

import java.util.List;

import iai.glsib.backend.dtos.AccountHistoryDTO;
import iai.glsib.backend.dtos.AccountOperationDTO;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;

public interface IAccountOperationService {
    List<AccountOperationDTO> accountHistory(String accountID);
    
    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
