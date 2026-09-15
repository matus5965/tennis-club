package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.entity.CourtType;

import java.util.Optional;

public interface CourtTypeDao extends GenericDao {
    Optional<CourtType> findByName(String name);
}
