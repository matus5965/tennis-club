package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.court.CourtResponse;
import cz.inqool.tennisclub.dto.court.CourtSaveRequest;
import cz.inqool.tennisclub.service.CourtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
@Tag(name = "Courts", description = "Operations for managing courts")
public class CourtController {

    private final CourtService service;

    public CourtController(CourtService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Creates new court")
    public ResponseEntity<CourtResponse> create(@Valid @RequestBody CourtSaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds court specified by id")
    public CourtResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    @Operation(summary = "Finds all courts")
    public List<CourtResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates court properties specified by id")
    public CourtResponse update(@PathVariable Long id, @Valid @RequestBody CourtSaveRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes court specified by id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
