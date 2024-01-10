package iai.glsib.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iai.glsib.backend.dtos.CustomerDTO;
import iai.glsib.backend.entities.Customer;
import iai.glsib.backend.exceptions.CustomerNotFoundException;
import iai.glsib.backend.mappers.BankAccountMapperImpl;
import iai.glsib.backend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomerService implements ICustomerService {

    private CustomerRepository customerRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDto) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.formCustomerDTO(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> collectCustomerDTOs = customers.stream()
                                                        .map(cust -> dtoMapper.fromCustomer(cust))
                                                        .collect(Collectors.toList());
        /* List<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
        for (Customer customer : customers) {
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOs.add(customerDTO);
        } */
        return collectCustomerDTOs;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDto) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.formCustomerDTO(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(cust -> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }
}
