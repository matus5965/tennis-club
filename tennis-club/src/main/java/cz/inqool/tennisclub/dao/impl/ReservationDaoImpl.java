package cz.inqool.tennisclub.dao.impl;

import cz.inqool.tennisclub.dao.AbstractDao;
import cz.inqool.tennisclub.dao.ReservationDao;
import cz.inqool.tennisclub.entity.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationDaoImpl extends AbstractDao<Reservation, Long> implements ReservationDao {

    public ReservationDaoImpl(EntityManager entityManager) {
        super(entityManager, Reservation.class);
    }

    @Override
    public List<Reservation> findByCourtIdOrderByCreatedAtAsc(Long courtId) {
        return entityManager.createQuery(
        "SELECT r FROM Reservation r WHERE r.court.id = :courtId AND r.deleted = false " +
                "ORDER BY r.createdAt ASC", Reservation.class)
            .setParameter("courtId", courtId)
            .getResultList();
    }

    @Override
    public List<Reservation> findByCustomerPhoneNumber(String phoneNumber, boolean futureOnly) {
        TypedQuery<Reservation> query = entityManager.createQuery(
            "SELECT r FROM Reservation r " +
                    "WHERE r.customer.phoneNumber = :phone AND r.deleted = false" +
                    (futureOnly ? " AND r.startTime > :now" : ""), Reservation.class)
            .setParameter("phone", phoneNumber);
        if (futureOnly) {
            query.setParameter("now", LocalDateTime.now());
        }
        return query.getResultList();
    }

    @Override
    public List<Reservation> findOverlapping(Long courtId, LocalDateTime start, LocalDateTime end) {
        return entityManager.createQuery(
        "SELECT r FROM Reservation r WHERE r.court.id = :courtId AND r.deleted = false " +
                "AND r.startTime < :end AND r.endTime > :start", Reservation.class)
            .setParameter("courtId", courtId)
            .setParameter("start", start)
            .setParameter("end", end)
            .getResultList();
    }
}
