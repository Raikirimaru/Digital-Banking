package iai.glsib.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import iai.glsib.backend.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByBankAccoundId(String accountId);
    Page<AccountOperation> findByBankAccoundPageId(String accountId, Pageable pageable);
}
