package iai.glsib.backend.dtos;

import java.util.Date;

import lombok.Data;


@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private Date DateOfBirth;
    private String tel;
}
