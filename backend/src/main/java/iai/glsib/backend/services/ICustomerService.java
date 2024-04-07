package iai.glsib.backend.services;

import java.util.List;

import iai.glsib.backend.dtos.CustomerDTO;
import iai.glsib.backend.dtos.CustomersDTO;
import iai.glsib.backend.exceptions.CustomerNotFoundException;

public interface ICustomerService<T> {
    T saveCustomer(T customerDto);
    
    List<T> listCustomers();

    // List<CustomerDTO> searchCustomers(String keyword);

    T getCustomer(Long customerId) throws CustomerNotFoundException;

    T updateCustomer(T customerDto);

    void deleteCustomer(Long customerId);

    List<T> listCustomers(int page);

    T getCustomerByName(String name);

    CustomersDTO getCustomerByName(String keyword, int page) throws CustomerNotFoundException;
}
