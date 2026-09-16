package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dao.CourtDao;
import cz.inqool.tennisclub.dao.CourtTypeDao;
import cz.inqool.tennisclub.dto.court.CourtResponse;
import cz.inqool.tennisclub.dto.court.CourtSaveRequest;
import cz.inqool.tennisclub.entity.Court;
import cz.inqool.tennisclub.entity.CourtType;
import cz.inqool.tennisclub.exception.NotFoundException;
import cz.inqool.tennisclub.mapper.CourtMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourtServiceImpl implements CourtService {

    private final CourtDao courtDao;
    private final CourtTypeDao courtTypeDao;
    private final CourtMapper mapper;

    public CourtServiceImpl(CourtDao courtDao, CourtTypeDao courtTypeDao, CourtMapper mapper) {
        this.courtDao = courtDao;
        this.courtTypeDao = courtTypeDao;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CourtResponse create(CourtSaveRequest request) {
        CourtType type = courtTypeDao.findById(request.courtTypeId())
                .orElseThrow(() -> new NotFoundException("Court type not found: " + request.courtTypeId()));

        Court court = new Court();
        court.setNumber(request.courtNumber());
        court.setCourtType(type);
        return mapper.toResponse(courtDao.save(court));
    }

    @Override
    @Transactional(readOnly = true)
    public CourtResponse getById(Long id) {
        return mapper.toResponse(courtDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Court not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourtResponse> getAll() {
        return courtDao.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public CourtResponse update(Long id, CourtSaveRequest request) {
        Court court = courtDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Court not found: " + id));
        CourtType type = courtTypeDao.findById(request.courtTypeId())
                .orElseThrow(() -> new NotFoundException("Court type not found: " + request.courtTypeId()));

        court.setNumber(request.courtNumber());
        court.setCourtType(type);
        return mapper.toResponse(courtDao.save(court));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Court court = courtDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Court not found: " + id));
        courtDao.delete(court);
    }
}
