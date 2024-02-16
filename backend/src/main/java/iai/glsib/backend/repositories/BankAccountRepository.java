package iai.glsib.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import iai.glsib.backend.entities.BankAccount;
import iai.glsib.backend.entities.Customer;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    Page<BankAccount> findAll(Pageable pageable);
    List<BankAccount> findByCustomer(Customer customer);
}
