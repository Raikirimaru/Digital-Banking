package iai.glsib.backend.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import iai.glsib.backend.dtos.CustomerDTO;
import iai.glsib.backend.entities.Customer;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer formCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
}
