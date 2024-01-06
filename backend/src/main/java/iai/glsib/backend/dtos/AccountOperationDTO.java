package iai.glsib.backend.dtos;

import java.util.Date;

import iai.glsib.backend.enums.OperationType;
import lombok.Data;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
