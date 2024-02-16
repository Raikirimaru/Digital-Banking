package iai.glsib.backend.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iai.glsib.backend.dtos.AccountHistoryDTO;
import iai.glsib.backend.dtos.AccountOperationDTO;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.services.AccountOperationService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class AccountOperationRestController {
    
    private AccountOperationService accountOperationService;

    @GetMapping("/accounts/{id}/operations")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<AccountOperationDTO> getHistory (@PathVariable(name = "id") String accountId) {
        return accountOperationService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{id}/pageOperations")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public AccountHistoryDTO getAccountOperationHistory (
        @PathVariable(name = "id") String accountId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size
        ) throws BankAccountNotFoundException {
        return accountOperationService.getAccountHistory(accountId, page, size);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/operations/Debit")
    public AccountOperationDTO debit(@RequestBody AccountOperationDTO accountOperarionDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.accountOperationService.debit(accountOperarionDTO.getAccountId(), accountOperarionDTO.getAmount(), accountOperarionDTO.getDescription());
        return accountOperarionDTO;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/operations/Credit")
    public AccountOperationDTO credit(@RequestBody AccountOperationDTO accountOperarionDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.accountOperationService.credit(accountOperarionDTO.getAccountId(), accountOperarionDTO.getAmount(), accountOperarionDTO.getDescription());
        return accountOperarionDTO;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/operations/Transfers")
    public void transfers(
            @RequestParam(name = "idSource") String idSource,
            @RequestParam(name = "idDestination") String idDestination, 
            @RequestParam(name = "amount") double amount,
            @RequestParam(name = "description") String description
            ) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.accountOperationService.transfer(idSource, idDestination, amount, description);
    }
}
