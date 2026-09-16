package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dao.CourtDao;
import cz.inqool.tennisclub.dao.ReservationDao;
import cz.inqool.tennisclub.dto.reservation.ReservationCreateRequest;
import cz.inqool.tennisclub.dto.reservation.ReservationResponse;
import cz.inqool.tennisclub.dto.reservation.ReservationUpdateRequest;
import cz.inqool.tennisclub.entity.Court;
import cz.inqool.tennisclub.entity.Customer;
import cz.inqool.tennisclub.entity.GameType;
import cz.inqool.tennisclub.entity.Reservation;
import cz.inqool.tennisclub.exception.BadRequestException;
import cz.inqool.tennisclub.exception.ConflictException;
import cz.inqool.tennisclub.exception.NotFoundException;
import cz.inqool.tennisclub.mapper.ReservationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final BigDecimal DOUBLES_MULTIPLIER = new BigDecimal("1.5");

    private final ReservationDao reservationDao;
    private final CourtDao courtDao;
    private final CustomerService customerService;
    private final ReservationMapper mapper;

    public ReservationServiceImpl(ReservationDao reservationDao,
                                  CourtDao courtDao,
                                  CustomerService customerService,
                                  ReservationMapper mapper) {
        this.reservationDao = reservationDao;
        this.courtDao = courtDao;
        this.customerService = customerService;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ReservationResponse create(ReservationCreateRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new BadRequestException("endTime must be after startTime");
        }

        Court court = courtDao.findByCourtNumber(request.courtNumber())
                .orElseThrow(() -> new NotFoundException("Court not found: " + request.courtNumber()));

        Customer customer = customerService.resolveOrCreate(request.phoneNumber(), request.customerName());

        assertNoOverlap(court.getId(), request.startTime(), request.endTime(), null);

        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setGameType(request.gameType());
        reservation.setPrice(calculatePrice(court, request.startTime(), request.endTime(), request.gameType()));

        return mapper.toResponse(reservationDao.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponse update(Long id, ReservationUpdateRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new BadRequestException("endTime must be after startTime");
        }

        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + id));

        assertNoOverlap(reservation.getCourt().getId(), request.startTime(), request.endTime(), id);

        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setGameType(request.gameType());
        reservation.setPrice(calculatePrice(
                reservation.getCourt(), request.startTime(), request.endTime(), request.gameType()));

        return mapper.toResponse(reservationDao.save(reservation));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + id));
        reservationDao.delete(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAll() {
        return reservationDao.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getByCourtNumber(String courtNumber) {
        Court court = courtDao.findByCourtNumber(courtNumber)
                .orElseThrow(() -> new NotFoundException("Court not found: " + courtNumber));
        return reservationDao.findByCourtIdOrderByCreatedAtAsc(court.getId())
                .stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getByPhoneNumber(String phoneNumber, boolean futureOnly) {
        return reservationDao.findByCustomerPhoneNumber(phoneNumber, futureOnly)
                .stream().map(mapper::toResponse).toList();
    }

    private void assertNoOverlap(Long courtId, LocalDateTime start, LocalDateTime end, Long excludeReservationId) {
        List<Reservation> overlapping = reservationDao.findOverlapping(courtId, start, end);
        boolean conflict = overlapping.stream()
                .anyMatch(r -> !r.getId().equals(excludeReservationId));
        if (conflict) {
            throw new ConflictException("Court is already booked in that time range");
        }
    }

    private BigDecimal calculatePrice(Court court, LocalDateTime start, LocalDateTime end, GameType gameType) {
        long minutes = Duration.between(start, end).toMinutes();
        BigDecimal base = court.getCourtType().getPricePerMinute()
                .multiply(BigDecimal.valueOf(minutes));
        return gameType == GameType.DOUBLES ? base.multiply(DOUBLES_MULTIPLIER) : base;
    }
}
