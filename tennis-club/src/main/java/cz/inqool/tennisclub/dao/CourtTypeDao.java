package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.entity.CourtType;
import cz.inqool.tennisclub.entity.Customer;

import java.util.Optional;

public interface CourtTypeDao extends GenericDao<CourtType, Long> {
    Optional<CourtType> findByName(String name);
}
