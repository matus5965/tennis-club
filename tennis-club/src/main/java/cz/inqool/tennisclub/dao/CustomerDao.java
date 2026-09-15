package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.entity.Customer;

import java.util.Optional;

public interface CustomerDao extends GenericDao {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
