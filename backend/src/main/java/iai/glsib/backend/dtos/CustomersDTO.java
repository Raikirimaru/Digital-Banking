package iai.glsib.backend.dtos;

import java.util.List;

import lombok.Data;

@Data
public class CustomersDTO {
    List<CustomerDTO> customerDTO;
    int totalpage ;
}
