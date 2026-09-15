package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationDao extends GenericDao<Reservation, Long> {
    List<Reservation> findByCourtIdOrderByCreatedAtAsc(Long courtId);
    List<Reservation> findByCustomerPhoneNumber(String phoneNumber, boolean futureOnly);
    List<Reservation> findOverlapping(Long courtId, LocalDateTime start, LocalDateTime end);
}
