package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.dao.impl.ReservationDaoImpl;
import cz.inqool.tennisclub.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ReservationDaoImpl.class)
public class ReservationDaoImplTest {

    @Autowired
    private ReservationDaoImpl reservationDao;

    @Autowired
    private TestEntityManager entityManager;

    private Court court;
    private Customer customer;

    @BeforeEach
    void setUp() {
        CourtType surface = new CourtType();
        surface.setName("Clay");
        surface.setPricePerMinute(BigDecimal.valueOf(10.0));
        entityManager.persist(surface);

        court = new Court();
        court.setNumber("Court 1");
        court.setCourtType(surface);
        entityManager.persist(court);

        customer = new Customer();
        customer.setName("John Doe");
        customer.setPhoneNumber("+421000000000");
        entityManager.persist(customer);
    }

    @Test
    public void findByCourtIdOrderByCreatedAtAsc_ExcludesDeleted() {
        Reservation r1 = createReservation(
                LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), false);
        Reservation r2 = createReservation(
                LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4), false);
        createReservation(LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6), true);

        List<Reservation> results = reservationDao.findByCourtIdOrderByCreatedAtAsc(court.getId());

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getId()).isEqualTo(r1.getId());
        assertThat(results.get(1).getId()).isEqualTo(r2.getId());
    }

    @Test
    public void findByCustomerPhoneNumber_FutureOnly() {
        Reservation past = createReservation(
                LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1), false);
        Reservation future = createReservation(
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), false);

        List<Reservation> futureResults = reservationDao.findByCustomerPhoneNumber(
                customer.getPhoneNumber(), true);
        List<Reservation> allResults = reservationDao.findByCustomerPhoneNumber(
                customer.getPhoneNumber(), false);

        assertThat(futureResults).hasSize(1);
        assertThat(futureResults.get(0).getId()).isEqualTo(future.getId());
        assertThat(allResults).hasSize(2);
    }

    @Test
    public void findOverlapping_DetectsConflict() {
        LocalDateTime start = LocalDateTime.now().plusHours(10);
        LocalDateTime end = start.plusHours(2);
        createReservation(start, end, false);

        List<Reservation> overlapStart = reservationDao.findOverlapping(
                court.getId(), start.minusMinutes(30), start.plusMinutes(30));
        List<Reservation> noOverlap = reservationDao.findOverlapping(
                court.getId(), start.minusHours(3), start.minusHours(1));

        assertThat(overlapStart).hasSize(1);
        assertThat(noOverlap).isEmpty();
    }

    private Reservation createReservation(LocalDateTime start, LocalDateTime end, boolean deleted) {
        Reservation r = new Reservation();
        r.setCourt(court);
        r.setCustomer(customer);
        r.setStartTime(start);
        r.setEndTime(end);
        r.setCreatedAt(LocalDateTime.now());
        r.setGameType(GameType.SINGLES);
        r.setPrice(new BigDecimal(0));
        r.setDeleted(deleted);
        entityManager.persist(r);
        entityManager.flush();
        return r;
    }
}