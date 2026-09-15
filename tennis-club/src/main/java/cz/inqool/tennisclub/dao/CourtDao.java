package cz.inqool.tennisclub.dao;

import cz.inqool.tennisclub.entity.Court;

import java.util.Optional;

public interface CourtDao extends GenericDao {
    Optional<Court> findByCourtNumber(String courtNumber);
}
