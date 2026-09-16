package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dao.CustomerDao;
import cz.inqool.tennisclub.entity.Customer;
import cz.inqool.tennisclub.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public Customer resolveOrCreate(String phoneNumber, String name) {
        Optional<Customer> existing = customerDao.findByPhoneNumber(phoneNumber);

        if (existing.isEmpty()) {
            Customer customer = new Customer();
            customer.setPhoneNumber(phoneNumber);
            customer.setName(name);
            return customerDao.save(customer);
        }

        Customer customer = existing.get();
        if (!customer.getName().equals(name)) {
            throw new BadRequestException(
                    "Phone number " + phoneNumber + " is already registered under a different name");
        }
        return customer;
    }
}
