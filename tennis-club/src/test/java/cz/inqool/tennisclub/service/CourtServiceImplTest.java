package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dao.CourtDao;
import cz.inqool.tennisclub.dao.CourtTypeDao;
import cz.inqool.tennisclub.dto.court.CourtResponse;
import cz.inqool.tennisclub.dto.court.CourtSaveRequest;
import cz.inqool.tennisclub.entity.Court;
import cz.inqool.tennisclub.entity.CourtType;
import cz.inqool.tennisclub.exception.NotFoundException;
import cz.inqool.tennisclub.mapper.CourtMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourtServiceImplTest {

    @Mock
    private CourtDao courtDao;

    @Mock
    private CourtTypeDao courtTypeDao;

    @Mock
    private CourtMapper mapper;

    @InjectMocks
    private CourtServiceImpl courtService;

    @Test
    public void create_Success() {
        CourtSaveRequest request = new CourtSaveRequest("Court 1", 1L);
        CourtType type = new CourtType();
        Court savedCourt = new Court();
        CourtResponse expected = new CourtResponse(1L, "Court 1", null, null);

        when(courtTypeDao.findById(1L)).thenReturn(Optional.of(type));
        when(courtDao.save(any(Court.class))).thenReturn(savedCourt);
        when(mapper.toResponse(savedCourt)).thenReturn(expected);

        CourtResponse result = courtService.create(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void create_CourtTypeNotFound_ThrowsException() {
        CourtSaveRequest request = new CourtSaveRequest("Court 1", 99L);
        when(courtTypeDao.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courtService.create(request))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void getById_Success() {
        Court court = new Court();
        CourtResponse expected = new CourtResponse(1L, "Court 1", null, null);

        when(courtDao.findById(1L)).thenReturn(Optional.of(court));
        when(mapper.toResponse(court)).thenReturn(expected);

        CourtResponse result = courtService.getById(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getById_NotFound_ThrowsException() {
        when(courtDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courtService.getById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void getAll_Success() {
        Court court = new Court();
        CourtResponse response = new CourtResponse(1L, "Court 1", null, null);

        when(courtDao.findAll()).thenReturn(List.of(court));
        when(mapper.toResponse(court)).thenReturn(response);

        List<CourtResponse> result = courtService.getAll();

        assertThat(result).containsExactly(response);
    }

    @Test
    public void update_Success() {
        CourtSaveRequest request = new CourtSaveRequest("Court 2", 2L);
        Court court = new Court();
        CourtType type = new CourtType();
        CourtResponse expected = new CourtResponse(1L, "Court 2", null, null);

        when(courtDao.findById(1L)).thenReturn(Optional.of(court));
        when(courtTypeDao.findById(2L)).thenReturn(Optional.of(type));
        when(courtDao.save(court)).thenReturn(court);
        when(mapper.toResponse(court)).thenReturn(expected);

        CourtResponse result = courtService.update(1L, request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void delete_Success() {
        Court court = new Court();
        when(courtDao.findById(1L)).thenReturn(Optional.of(court));

        courtService.delete(1L);

        verify(courtDao).delete(court);
    }

    @Test
    public void delete_NotFound_ThrowsException() {
        when(courtDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courtService.delete(1L))
                .isInstanceOf(NotFoundException.class);
    }
}