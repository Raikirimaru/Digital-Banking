package iai.glsib.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import iai.glsib.backend.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    
}
