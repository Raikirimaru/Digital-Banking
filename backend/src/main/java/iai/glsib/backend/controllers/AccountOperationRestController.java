package iai.glsib.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iai.glsib.backend.dtos.AccountHistoryDTO;
import iai.glsib.backend.dtos.AccountOperationDTO;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.services.BankAccountService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class AccountOperationRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDTO> getHistory (@PathVariable(name = "id") String accountId) {
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDTO getAccountHistory (
        @PathVariable(name = "id") String accountId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size
        ) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }
}
