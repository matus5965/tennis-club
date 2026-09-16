package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dto.court.CourtResponse;
import cz.inqool.tennisclub.dto.court.CourtSaveRequest;

import java.util.List;

public interface CourtService {
    CourtResponse create(CourtSaveRequest request);
    CourtResponse getById(Long id);
    List<CourtResponse> getAll();
    CourtResponse update(Long id, CourtSaveRequest request);
    void delete(Long id);
}
