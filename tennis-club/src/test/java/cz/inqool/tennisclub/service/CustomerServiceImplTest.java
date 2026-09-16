package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dao.CustomerDao;
import cz.inqool.tennisclub.entity.Customer;
import cz.inqool.tennisclub.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerDao customerDao;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void resolveOrCreate_NewCustomer_Success() {
        when(customerDao.findByPhoneNumber("+421000000000")).thenReturn(Optional.empty());
        Customer savedCustomer = new Customer();
        when(customerDao.save(any(Customer.class))).thenReturn(savedCustomer);

        Customer result = customerService.resolveOrCreate("+421000000000", "John Doe");

        assertThat(result).isEqualTo(savedCustomer);
    }

    @Test
    public void resolveOrCreate_DifferentName_ThrowsBadRequestException() {
        Customer existing = new Customer();
        existing.setName("John Doe");
        existing.setPhoneNumber("+421000000000");
        when(customerDao.findByPhoneNumber("+421000000000")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> customerService.resolveOrCreate("+421000000000", "Jane Doe"))
                .isInstanceOf(BadRequestException.class);
    }
}