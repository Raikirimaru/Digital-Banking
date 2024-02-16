package iai.glsib.backend.dtos;

import java.util.List;

import lombok.Data;

@Data
public class BankAccountsDTO {
    List<BankAccountDTO> bankAccountDTOS;
    int totalPage;
}
