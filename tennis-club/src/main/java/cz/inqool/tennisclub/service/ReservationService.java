package cz.inqool.tennisclub.service;

import cz.inqool.tennisclub.dto.reservation.ReservationCreateRequest;
import cz.inqool.tennisclub.dto.reservation.ReservationResponse;
import cz.inqool.tennisclub.dto.reservation.ReservationUpdateRequest;

import java.util.List;

public interface ReservationService {
    ReservationResponse create(ReservationCreateRequest request);
    ReservationResponse update(Long id, ReservationUpdateRequest request);
    void delete(Long id);
    List<ReservationResponse> getAll();
    List<ReservationResponse> getByCourtNumber(String courtNumber);
    List<ReservationResponse> getByPhoneNumber(String phoneNumber, boolean futureOnly);
}