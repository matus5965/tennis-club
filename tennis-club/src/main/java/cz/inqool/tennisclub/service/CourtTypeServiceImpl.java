package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dao.CourtTypeDao;
import cz.inqool.tennisclub.dto.courttype.CourtTypeResponse;
import cz.inqool.tennisclub.dto.courttype.CourtTypeSaveRequest;
import cz.inqool.tennisclub.entity.CourtType;
import cz.inqool.tennisclub.exception.NotFoundException;
import cz.inqool.tennisclub.mapper.CourtTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourtTypeServiceImpl implements CourtTypeService {

    private final CourtTypeDao courtTypeDao;
    private final CourtTypeMapper mapper;

    public CourtTypeServiceImpl(CourtTypeDao courtTypeDao, CourtTypeMapper mapper) {
        this.courtTypeDao = courtTypeDao;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CourtTypeResponse create(CourtTypeSaveRequest request) {
        CourtType type = new CourtType();
        type.setName(request.name());
        type.setPricePerMinute(request.pricePerMinute());
        return mapper.toResponse(courtTypeDao.save(type));
    }

    @Override
    @Transactional(readOnly = true)
    public CourtTypeResponse getById(Long id) {
        CourtType type = courtTypeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Court type not found: " + id));
        return mapper.toResponse(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourtTypeResponse> getAll() {
        return courtTypeDao.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public CourtTypeResponse update(Long id, CourtTypeSaveRequest request) {
        CourtType type = courtTypeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Court type not found: " + id));
        type.setName(request.name());
        type.setPricePerMinute(request.pricePerMinute());
        return mapper.toResponse(courtTypeDao.save(type)); // dirty checking would even cover this without save()
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CourtType type = courtTypeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Court type not found: " + id));
        courtTypeDao.delete(type);
    }
}
