package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.court.CourtResponse;
import cz.inqool.tennisclub.dto.court.CourtSaveRequest;
import cz.inqool.tennisclub.service.CourtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    private final CourtService service;

    public CourtController(CourtService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CourtResponse> create(@Valid @RequestBody CourtSaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public CourtResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<CourtResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public CourtResponse update(@PathVariable Long id, @Valid @RequestBody CourtSaveRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
