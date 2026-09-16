package cz.inqool.tennisclub.controller;

import cz.inqool.tennisclub.dto.courttype.CourtTypeResponse;
import cz.inqool.tennisclub.dto.courttype.CourtTypeSaveRequest;
import cz.inqool.tennisclub.service.CourtTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/court-types")
@Tag(name = "Court types", description = "Operations for managing court types")
public class CourtTypeController {

    private final CourtTypeService service;

    public CourtTypeController(CourtTypeService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Creates new court type")
    public ResponseEntity<CourtTypeResponse> create(@Valid @RequestBody CourtTypeSaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds court type specified by id")
    public CourtTypeResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    @Operation(summary = "Finds all court types")
    public List<CourtTypeResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates court type properties specified by id")
    public CourtTypeResponse update(@PathVariable Long id, @Valid @RequestBody CourtTypeSaveRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes court type specified by id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
