package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dao.CourtTypeDao;
import cz.inqool.tennisclub.dto.courttype.CourtTypeResponse;
import cz.inqool.tennisclub.dto.courttype.CourtTypeSaveRequest;
import cz.inqool.tennisclub.entity.CourtType;
import cz.inqool.tennisclub.exception.NotFoundException;
import cz.inqool.tennisclub.mapper.CourtTypeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourtTypeServiceImplTest {

    @Mock
    private CourtTypeDao courtTypeDao;

    @Mock
    private CourtTypeMapper mapper;

    @InjectMocks
    private CourtTypeServiceImpl courtTypeService;

    @Test
    public void create_Success() {
        CourtTypeSaveRequest request = new CourtTypeSaveRequest("Grass", BigDecimal.valueOf(10.0));
        CourtType savedType = new CourtType();
        CourtTypeResponse expected = new CourtTypeResponse(1L, "Grass", BigDecimal.valueOf(10.0));

        when(courtTypeDao.save(any(CourtType.class))).thenReturn(savedType);
        when(mapper.toResponse(savedType)).thenReturn(expected);

        CourtTypeResponse result = courtTypeService.create(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getById_Success() {
        CourtType type = new CourtType();
        CourtTypeResponse expected = new CourtTypeResponse(1L, "Grass", BigDecimal.valueOf(10.0));

        when(courtTypeDao.findById(1L)).thenReturn(Optional.of(type));
        when(mapper.toResponse(type)).thenReturn(expected);

        CourtTypeResponse result = courtTypeService.getById(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getById_NotFound_ThrowsException() {
        when(courtTypeDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courtTypeService.getById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void getAll_Success() {
        CourtType type = new CourtType();
        CourtTypeResponse response = new CourtTypeResponse(1L, "Grass", BigDecimal.valueOf(10.0));

        when(courtTypeDao.findAll()).thenReturn(List.of(type));
        when(mapper.toResponse(type)).thenReturn(response);

        List<CourtTypeResponse> result = courtTypeService.getAll();

        assertThat(result).containsExactly(response);
    }

    @Test
    public void update_Success() {
        CourtTypeSaveRequest request = new CourtTypeSaveRequest("Clay", BigDecimal.valueOf(15.0));
        CourtType type = new CourtType();
        CourtTypeResponse expected = new CourtTypeResponse(1L, "Clay", BigDecimal.valueOf(15.0));

        when(courtTypeDao.findById(1L)).thenReturn(Optional.of(type));
        when(courtTypeDao.save(type)).thenReturn(type);
        when(mapper.toResponse(type)).thenReturn(expected);

        CourtTypeResponse result = courtTypeService.update(1L, request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void update_NotFound_ThrowsException() {
        CourtTypeSaveRequest request = new CourtTypeSaveRequest("Clay", BigDecimal.valueOf(15.0));
        when(courtTypeDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courtTypeService.update(1L, request))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void delete_Success() {
        CourtType type = new CourtType();
        when(courtTypeDao.findById(1L)).thenReturn(Optional.of(type));

        courtTypeService.delete(1L);

        verify(courtTypeDao).delete(type);
    }

    @Test
    public void delete_NotFound_ThrowsException() {
        when(courtTypeDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courtTypeService.delete(1L))
                .isInstanceOf(NotFoundException.class);
    }
}