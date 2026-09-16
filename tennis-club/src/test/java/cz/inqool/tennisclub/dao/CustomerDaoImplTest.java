package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.dao.impl.CustomerDaoImpl;
import cz.inqool.tennisclub.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CustomerDaoImpl.class)
public class CustomerDaoImplTest {

    @Autowired
    private CustomerDaoImpl customerDao;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByPhoneNumber_WhenExistsAndNotDeleted_ReturnsCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setPhoneNumber("+421000000000");
        customer.setDeleted(false);
        entityManager.persistAndFlush(customer);

        Optional<Customer> result = customerDao.findByPhoneNumber("+421000000000");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");
        assertThat(result.get().getPhoneNumber()).isEqualTo("+421000000000");
    }

    @Test
    public void findByPhoneNumber_WhenNotFound_ReturnsEmpty() {
        Optional<Customer> result = customerDao.findByPhoneNumber("+000000000");

        assertThat(result).isEmpty();
    }
}