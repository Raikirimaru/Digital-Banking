package iai.glsib.backend.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import iai.glsib.backend.dtos.AccountOperationDTO;
import iai.glsib.backend.dtos.CurrentBankAccountDTO;
import iai.glsib.backend.dtos.CustomerDTO;
import iai.glsib.backend.dtos.SavingBankAccountDTO;
import iai.glsib.backend.entities.AccountOperation;
import iai.glsib.backend.entities.CurrentAccount;
import iai.glsib.backend.entities.Customer;
import iai.glsib.backend.entities.SavingAccount;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        if (customer == null) 
            throw new IllegalArgumentException("Input customer cannot be null");
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
    

    public Customer formCustomerDTO(CustomerDTO customerDTO) {
        if (customerDTO == null) 
            throw new IllegalArgumentException("Input customerDTO cannot be null");
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingAccount(SavingAccount savingAccount) {
        if (savingAccount == null) 
            throw new IllegalArgumentException("Input saving Account cannot be null");
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO) {
        if (savingBankAccountDTO == null) 
            throw new IllegalArgumentException("Input saving Account DTO cannot be null");
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);
        savingAccount.setCustomer(formCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    public CurrentBankAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
        if (currentAccount == null) 
            throw new IllegalArgumentException("Input currentAccount cannot be null");
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer())); 
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO) {
        if (currentBankAccountDTO == null) 
            throw new IllegalArgumentException("Input currentBankAccountDTO cannot be null");
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
        currentAccount.setCustomer(formCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return null;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        if (accountOperation == null) 
            throw new IllegalArgumentException("Input accountOperation cannot be null");
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, accountOperationDTO);
        return accountOperationDTO;
    }

   /*  public AccountOperation fromAccountOperationDTO(AccountOperationDTO accountOperationDTO) {
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO, accountOperation);
        return accountOperation;
    } */
}
