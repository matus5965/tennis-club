package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.entity.Customer;

import java.util.Optional;

public interface CustomerDao extends GenericDao<Customer, Long> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
