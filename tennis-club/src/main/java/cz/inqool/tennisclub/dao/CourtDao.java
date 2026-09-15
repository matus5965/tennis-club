package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.entity.Court;
import cz.inqool.tennisclub.entity.Customer;

import java.util.Optional;

public interface CourtDao extends GenericDao<Court, Long> {
    Optional<Court> findByCourtNumber(String courtNumber);
}
