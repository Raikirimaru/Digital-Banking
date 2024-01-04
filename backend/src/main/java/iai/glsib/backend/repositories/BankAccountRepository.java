package iai.glsib.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import iai.glsib.backend.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    
}
