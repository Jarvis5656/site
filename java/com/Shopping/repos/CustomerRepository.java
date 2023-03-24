package com.Shopping.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Shopping.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{

}
