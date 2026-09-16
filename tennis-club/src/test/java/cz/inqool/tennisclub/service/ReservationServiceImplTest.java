package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dao.CourtDao;
import cz.inqool.tennisclub.dao.ReservationDao;
import cz.inqool.tennisclub.dto.reservation.ReservationCreateRequest;
import cz.inqool.tennisclub.dto.reservation.ReservationResponse;
import cz.inqool.tennisclub.dto.reservation.ReservationUpdateRequest;
import cz.inqool.tennisclub.entity.Court;
import cz.inqool.tennisclub.entity.CourtType;
import cz.inqool.tennisclub.entity.Customer;
import cz.inqool.tennisclub.entity.GameType;
import cz.inqool.tennisclub.entity.Reservation;
import cz.inqool.tennisclub.exception.BadRequestException;
import cz.inqool.tennisclub.exception.ConflictException;
import cz.inqool.tennisclub.exception.NotFoundException;
import cz.inqool.tennisclub.mapper.ReservationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private CourtDao courtDao;

    @Mock
    private CustomerService customerService;

    @Mock
    private ReservationMapper mapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    public void create_Success_Singles() {
        LocalDateTime now = LocalDateTime.now();
        ReservationCreateRequest request = new ReservationCreateRequest(
                "Court 1", "+421000000000", "John Doe",
                now, now.plusHours(1), GameType.SINGLES);

        Court court = createCourt("Court 1", BigDecimal.valueOf(2.0));
        Customer customer = new Customer();
        Reservation saved = new Reservation();
        ReservationResponse expected = new ReservationResponse(
                1L, null, null, null,
                now, now.plusHours(1), GameType.SINGLES, BigDecimal.valueOf(120.0), null);

        when(courtDao.findByCourtNumber("Court 1")).thenReturn(Optional.of(court));
        when(customerService.resolveOrCreate("+421000000000", "John Doe")).thenReturn(customer);
        when(reservationDao.findOverlapping(court.getId(), now, now.plusHours(1))).thenReturn(Collections.emptyList());
        when(reservationDao.save(any(Reservation.class))).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(expected);

        ReservationResponse result = reservationService.create(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void create_Success_DoublesPriceMultiplier() {
        LocalDateTime now = LocalDateTime.now();
        ReservationCreateRequest request = new ReservationCreateRequest(
                "Court 1", "+421000000000", "John Doe",
                now, now.plusHours(1), GameType.DOUBLES);

        Court court = createCourt("Court 1", BigDecimal.valueOf(2.0));
        Customer customer = new Customer();
        Reservation saved = new Reservation();
        ReservationResponse expected = new ReservationResponse(
                1L, null, null, null,
                now, now.plusHours(1), GameType.SINGLES, BigDecimal.valueOf(180.0), null);

        when(courtDao.findByCourtNumber("Court 1")).thenReturn(Optional.of(court));
        when(customerService.resolveOrCreate("+421000000000", "John Doe")).thenReturn(customer);
        when(reservationDao.findOverlapping(court.getId(), now, now.plusHours(1))).thenReturn(Collections.emptyList());
        when(reservationDao.save(any(Reservation.class))).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(expected);

        ReservationResponse result = reservationService.create(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void create_EndTimeBeforeStartTime_ThrowsBadRequest() {
        LocalDateTime now = LocalDateTime.now();
        ReservationCreateRequest request = new ReservationCreateRequest(
                "Court 1", "+421000000000", "John Doe",
                now.plusHours(1), now, GameType.SINGLES);

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    public void create_CourtNotFound_ThrowsNotFound() {
        LocalDateTime now = LocalDateTime.now();
        ReservationCreateRequest request = new ReservationCreateRequest(
                "Court 99", "+421000000000", "John Doe",
                now, now.plusHours(1), GameType.SINGLES);

        when(courtDao.findByCourtNumber("Court 99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void create_OverlappingReservation_ThrowsConflict() {
        LocalDateTime now = LocalDateTime.now();
        ReservationCreateRequest request = new ReservationCreateRequest(
                "Court 1", "+421000000000", "John Doe",
                now, now.plusHours(1), GameType.SINGLES);

        Court court = createCourt("Court 1", BigDecimal.valueOf(2.0));
        Reservation existingConflict = new Reservation();
        existingConflict.setId(10L);

        when(courtDao.findByCourtNumber("Court 1")).thenReturn(Optional.of(court));
        when(customerService.resolveOrCreate("+421000000000", "John Doe")).thenReturn(new Customer());
        when(reservationDao.findOverlapping(court.getId(), now, now.plusHours(1)))
                .thenReturn(List.of(existingConflict));

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    public void update_Success() {
        LocalDateTime now = LocalDateTime.now();
        ReservationUpdateRequest request = new ReservationUpdateRequest(now, now.plusHours(1), GameType.SINGLES);

        Court court = createCourt("Court 1", BigDecimal.valueOf(2.0));
        Reservation existing = new Reservation();
        existing.setId(1L);
        existing.setCourt(court);

        Reservation saved = new Reservation();
        ReservationResponse expected = new ReservationResponse(
                1L, null, null, null,
                now, now.plusHours(1), GameType.SINGLES, BigDecimal.valueOf(120.0), null);

        when(reservationDao.findById(1L)).thenReturn(Optional.of(existing));
        when(reservationDao.findOverlapping(court.getId(), now, now.plusHours(1))).thenReturn(List.of(existing));
        when(reservationDao.save(existing)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(expected);

        ReservationResponse result = reservationService.update(1L, request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void delete_Success() {
        Reservation reservation = new Reservation();
        when(reservationDao.findById(1L)).thenReturn(Optional.of(reservation));

        reservationService.delete(1L);

        verify(reservationDao).delete(reservation);
    }

    @Test
    public void delete_NotFound_ThrowsNotFound() {
        when(reservationDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.delete(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void getAll_Success() {
        Reservation reservation = new Reservation();
        ReservationResponse expected = new ReservationResponse(
                1L, null, null, null,
                null, null, null, null, null);

        when(reservationDao.findAll()).thenReturn(List.of(reservation));
        when(mapper.toResponse(reservation)).thenReturn(expected);

        List<ReservationResponse> result = reservationService.getAll();

        assertThat(result).containsExactly(expected);
    }

    @Test
    public void getByCourtNumber_Success() {
        Court court = createCourt("Court 1", BigDecimal.valueOf(2.0));
        Reservation reservation = new Reservation();
        ReservationResponse expected = new ReservationResponse(
                1L, null, null, null,
                null, null, null, null, null);

        when(courtDao.findByCourtNumber("Court 1")).thenReturn(Optional.of(court));
        when(reservationDao.findByCourtIdOrderByCreatedAtAsc(court.getId())).thenReturn(List.of(reservation));
        when(mapper.toResponse(reservation)).thenReturn(expected);

        List<ReservationResponse> result = reservationService.getByCourtNumber("Court 1");

        assertThat(result).containsExactly(expected);
    }

    @Test
    public void getByPhoneNumber_Success() {
        Reservation reservation = new Reservation();
        ReservationResponse expected = new ReservationResponse(
                1L, null, null, null,
                null, null, null, null, null);

        when(reservationDao.findByCustomerPhoneNumber("+421000000000", true))
                .thenReturn(List.of(reservation));
        when(mapper.toResponse(reservation)).thenReturn(expected);

        List<ReservationResponse> result = reservationService.getByPhoneNumber(
                "+421000000000", true);

        assertThat(result).containsExactly(expected);
    }

    private Court createCourt(String number, BigDecimal pricePerMinute) {
        CourtType type = new CourtType();
        type.setPricePerMinute(pricePerMinute);

        Court court = new Court();
        court.setId(5L);
        court.setNumber(number);
        court.setCourtType(type);
        return court;
    }
}