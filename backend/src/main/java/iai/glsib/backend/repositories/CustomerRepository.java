package iai.glsib.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import iai.glsib.backend.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    /* @Query("select c from Customer c where c.name like :kw")
    List<Customer> searchCustomer(@Param("kw") String keyword); */

    Page<Customer> findAll(Pageable pageable);

    Customer findByName(String name);

    @Query("select c from Customer c where c.name like :kw")
    Page<Customer> searchByName(@Param("kw") String keyword, Pageable pageable);
}
