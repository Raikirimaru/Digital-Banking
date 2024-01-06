package iai.glsib.backend.dtos;

import java.util.List;

import lombok.Data;

@Data
public class AccountHistoryDTO {
    private String accounId;
    private double balance;
    private int currentPage;
    private int totalPage;
    private int size;
    private List<AccountOperationDTO> accountOperationDTOs;
}
