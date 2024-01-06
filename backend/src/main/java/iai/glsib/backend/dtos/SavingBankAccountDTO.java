package iai.glsib.backend.dtos;

import java.util.Date;

import iai.glsib.backend.enums.AccountStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class SavingBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDto;
    private double interestRate;
}
