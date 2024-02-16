package iai.glsib.backend.controllers;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iai.glsib.backend.dtos.BankAccountDTO;
import iai.glsib.backend.dtos.BankAccountsDTO;
import iai.glsib.backend.dtos.CreditDTO;
import iai.glsib.backend.dtos.CurrentBankAccountDTO;
import iai.glsib.backend.dtos.DebitDTO;
import iai.glsib.backend.dtos.SavingBankAccountDTO;
import iai.glsib.backend.dtos.TransferRequestDTO;
import iai.glsib.backend.exceptions.BalanceNotSufficientException;
import iai.glsib.backend.exceptions.BankAccountNotFoundException;
import iai.glsib.backend.exceptions.CustomerNotFoundException;
import iai.glsib.backend.services.BankAccountService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public BankAccountDTO getBankAccount(@PathVariable(name = "id") String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<BankAccountDTO> listbankAccounts() {
        return bankAccountService.listbankAccounts();   
    }

    @PostMapping("/accounts/debit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount(),
                transferRequestDTO.getDescription()
            );
    }

    @PostMapping("/accounts/current")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<CurrentBankAccountDTO> createCurrentBankAccount(
            @RequestParam double initialBalance,
            @RequestParam double overdraft,
            @RequestParam Long customerId
        ) {
        try {
            CurrentBankAccountDTO accountDTO = bankAccountService.saveCurrentBankAccount(initialBalance, overdraft, customerId);
            return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/accounts/saving")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<SavingBankAccountDTO> createSavingBankAccount(
            @RequestParam double initialBalance,
            @RequestParam double interestRate,
            @RequestParam Long customerId
        ) {
        try {
            SavingBankAccountDTO accountDTO = bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
            return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/accounts/searchAccount")
    public BankAccountsDTO getBankAccount(@RequestParam(name = "page", defaultValue = "0") int page) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccountList(page);
    }
}