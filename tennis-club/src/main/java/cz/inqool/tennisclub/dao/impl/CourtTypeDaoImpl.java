package cz.inqool.tennisclub.dao.impl;

import cz.inqool.tennisclub.dao.AbstractDao;
import cz.inqool.tennisclub.dao.CourtTypeDao;
import cz.inqool.tennisclub.entity.CourtType;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourtTypeDaoImpl extends AbstractDao<CourtType, Long> implements CourtTypeDao {

    public CourtTypeDaoImpl(EntityManager entityManager) {
        super(entityManager, CourtType.class);
    }

    @Override
    public Optional<CourtType> findByName(String name) {
        return entityManager.createQuery(
        "SELECT c FROM CourtType c WHERE c.name = :name AND c.deleted = false", CourtType.class)
            .setParameter("name", name)
            .getResultStream()
            .findFirst();
    }
}
