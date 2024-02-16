package iai.glsib.backend.dtos;

import java.util.Date;

import iai.glsib.backend.enums.AccountStatus;
import lombok.Data;

@Data
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
    private String type ;
    private double interestRate ;
}
