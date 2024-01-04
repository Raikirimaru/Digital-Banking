package iai.glsib.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import iai.glsib.backend.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    
}
