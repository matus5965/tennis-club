package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dto.courttype.CourtTypeResponse;
import cz.inqool.tennisclub.dto.courttype.CourtTypeSaveRequest;

import java.util.List;

public interface CourtTypeService {
    CourtTypeResponse create(CourtTypeSaveRequest request);
    CourtTypeResponse getById(Long id);
    List<CourtTypeResponse> getAll();
    CourtTypeResponse update(Long id, CourtTypeSaveRequest request);
    void delete(Long id);
}
