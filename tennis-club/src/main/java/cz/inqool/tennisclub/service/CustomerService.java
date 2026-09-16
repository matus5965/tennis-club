package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.entity.Customer;

public interface CustomerService {
    Customer resolveOrCreate(String phoneNumber, String name);
}
