package cz.inqool.tennisclub.dao.impl;

import cz.inqool.tennisclub.dao.AbstractDao;
import cz.inqool.tennisclub.dao.CourtDao;
import cz.inqool.tennisclub.entity.Court;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourtDaoImpl extends AbstractDao<Court, Long> implements CourtDao {

    public CourtDaoImpl(EntityManager entityManager) {
        super(entityManager, Court.class);
    }

    @Override
    public Optional<Court> findByCourtNumber(String courtNumber) {
        return entityManager.createQuery(
        "SELECT c FROM Court c WHERE c.number = :num AND c.deleted = false", Court.class)
            .setParameter("num", courtNumber)
            .getResultStream()
            .findFirst();
    }
}
