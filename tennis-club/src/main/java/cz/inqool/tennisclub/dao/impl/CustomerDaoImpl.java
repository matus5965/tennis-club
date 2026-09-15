package cz.inqool.tennisclub.dao.impl;

import cz.inqool.tennisclub.dao.AbstractDao;
import cz.inqool.tennisclub.dao.CustomerDao;
import cz.inqool.tennisclub.entity.Customer;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerDaoImpl extends AbstractDao<Customer, Long> implements CustomerDao {

    public CustomerDaoImpl(EntityManager entityManager) {
        super(entityManager, Customer.class);
    }

    @Override
    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        return entityManager.createQuery(
        "SELECT c FROM Customer c WHERE c.phoneNumber = :phone AND c.deleted = false", Customer.class)
            .setParameter("phone", phoneNumber)
            .getResultStream()
            .findFirst();
    }
}
