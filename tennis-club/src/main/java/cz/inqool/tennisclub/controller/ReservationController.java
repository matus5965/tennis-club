package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.reservation.ReservationCreateRequest;
import cz.inqool.tennisclub.dto.reservation.ReservationResponse;
import cz.inqool.tennisclub.dto.reservation.ReservationUpdateRequest;
import cz.inqool.tennisclub.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ReservationResponse update(@PathVariable Long id, @Valid @RequestBody ReservationUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<ReservationResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/court/{courtNumber}")
    public List<ReservationResponse> getByCourtNumber(@PathVariable String courtNumber) {
        return service.getByCourtNumber(courtNumber);
    }

    @GetMapping("/customer/{phoneNumber}")
    public List<ReservationResponse> getByPhoneNumber(
            @PathVariable String phoneNumber,
            @RequestParam(defaultValue = "false") boolean futureOnly) {
        return service.getByPhoneNumber(phoneNumber, futureOnly);
    }
}
