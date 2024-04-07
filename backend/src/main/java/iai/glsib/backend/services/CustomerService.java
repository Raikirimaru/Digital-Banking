package iai.glsib.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iai.glsib.backend.dtos.CustomerDTO;
import iai.glsib.backend.dtos.CustomersDTO;
import iai.glsib.backend.entities.Customer;
import iai.glsib.backend.exceptions.CustomerNotFoundException;
import iai.glsib.backend.mappers.BankAccountMapperImpl;
import iai.glsib.backend.repositories.BankAccountRepository;
import iai.glsib.backend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomerService implements ICustomerService<CustomerDTO> {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(@NonNull CustomerDTO customerDto) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.formCustomerDTO(customerDto);
        if (customer == null) {
            throw new IllegalArgumentException("Invalid customer DTO");
        }
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
    public CustomerDTO getCustomer(@NonNull Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(@NonNull CustomerDTO customerDto) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.formCustomerDTO(customerDto);
        if (customer == null) {
            throw new IllegalArgumentException("Invalid customer DTO");
        }
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    

    @Override
    public void deleteCustomer(@NonNull Long customerId) {
        customerRepository.deleteById(customerId);
    }

    /* @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(cust -> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    } */

    @Override
    public CustomersDTO getCustomerByName(String keyword, int page) throws CustomerNotFoundException {
        Page<Customer> customers;
        customers = customerRepository.searchByName(keyword, PageRequest.of(page, 5));
        List<CustomerDTO> customerDTOS = customers.getContent().stream().map(c -> dtoMapper.fromCustomer(c)).collect(Collectors.toList());
        if (customers == null) throw new CustomerNotFoundException("customer not fount");
        CustomersDTO customersDTO= new CustomersDTO();
        customersDTO.setCustomerDTO(customerDTOS);
        customersDTO.setTotalpage(customers.getTotalPages());
        return customersDTO;
    }

    @Override
    public List<CustomerDTO> listCustomers(int page) {
        Page<Customer> customers = customerRepository.findAll(PageRequest.of(page , 6));
        List<CustomerDTO> collect = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public CustomerDTO getCustomerByName(String name) {
        Customer customer = customerRepository.findByName(name);
        return  dtoMapper.fromCustomer(customer);
    }

}
