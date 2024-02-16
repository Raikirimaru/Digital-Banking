package iai.glsib.backend.services;

import java.util.List;

import iai.glsib.backend.dtos.CustomerDTO;
import iai.glsib.backend.dtos.CustomersDTO;
import iai.glsib.backend.exceptions.CustomerNotFoundException;

public interface ICustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDto);
    
    List<CustomerDTO> listCustomers();

    // List<CustomerDTO> searchCustomers(String keyword);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDto);

    void deleteCustomer(Long customerId);

    List<CustomerDTO> listCustomers(int page);

    CustomerDTO getCustomerByName(String name);

    CustomersDTO getCustomerByName(String keyword, int page) throws CustomerNotFoundException;
}
