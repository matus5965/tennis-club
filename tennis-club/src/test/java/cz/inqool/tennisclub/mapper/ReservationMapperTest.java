package cz.inqool.tennisclub.mapper;

import cz.inqool.tennisclub.dto.reservation.ReservationResponse;
import cz.inqool.tennisclub.entity.Court;
import cz.inqool.tennisclub.entity.Customer;
import cz.inqool.tennisclub.entity.GameType;
import cz.inqool.tennisclub.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationMapperTest {

    private ReservationMapper reservationMapper;

    @BeforeEach
    void setUp() {
        reservationMapper = new ReservationMapper();
    }

    @Test
    void toResponse_ShouldMapAllFieldsCorrectly() {
        Court court = new Court();
        court.setNumber("Court 1");

        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setPhoneNumber("+421000000000");

        LocalDateTime now = LocalDateTime.now();

        Reservation entity = new Reservation();
        entity.setId(1L);
        entity.setCourt(court);
        entity.setCustomer(customer);
        entity.setStartTime(now.plusHours(1));
        entity.setEndTime(now.plusHours(2));
        entity.setGameType(GameType.SINGLES);
        entity.setPrice(new BigDecimal("15.50"));
        entity.setCreatedAt(now);

        ReservationResponse response = reservationMapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(entity.getId(), response.id());
        assertEquals(court.getNumber(), response.courtNumber());
        assertEquals(customer.getName(), response.customerName());
        assertEquals(customer.getPhoneNumber(), response.phoneNumber());
        assertEquals(entity.getStartTime(), response.startTime());
        assertEquals(entity.getEndTime(), response.endTime());
        assertEquals(entity.getGameType(), response.gameType());
        assertEquals(entity.getPrice(), response.price());
        assertEquals(entity.getCreatedAt(), response.createdAt());
    }
}