package com.saferide.insurance.controller;

import com.saferide.insurance.dto.request.InspectionRequest;
import com.saferide.insurance.dto.response.ApiResponse;
import com.saferide.insurance.entity.inspection.Inspection;
import com.saferide.insurance.service.InspectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inspections")
@CrossOrigin(origins = "*")
public class InspectionController {

    private final InspectionService inspectionService;

    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Inspection>> createInspection(@Valid @RequestBody InspectionRequest request) {
        Inspection inspection = inspectionService.createInspection(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inspection created successfully! Ref: " + inspection.getInspectionReference(), inspection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Inspection>> getInspectionById(@PathVariable Long id) {
        Inspection inspection = inspectionService.getInspectionById(id);
        return ResponseEntity.ok(ApiResponse.success("Inspection details retrieved", inspection));
    }

    @GetMapping("/reference/{inspectionReference}")
    public ResponseEntity<ApiResponse<Inspection>> getInspectionByReference(@PathVariable String inspectionReference) {
        Inspection inspection = inspectionService.getInspectionByReference(inspectionReference);
        return ResponseEntity.ok(ApiResponse.success("Inspection details retrieved", inspection));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<ApiResponse<List<Inspection>>> getInspectionsByVehicleId(@PathVariable Long vehicleId) {
        List<Inspection> inspections = inspectionService.getInspectionsByVehicleId(vehicleId);
        return ResponseEntity.ok(ApiResponse.success("Vehicle inspection history retrieved", inspections));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Inspection>>> getAllInspections() {
        List<Inspection> inspections = inspectionService.getAllInspections();
        return ResponseEntity.ok(ApiResponse.success("All inspection records retrieved", inspections));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Inspection>> updateInspection(@PathVariable Long id,
                                                                    @Valid @RequestBody InspectionRequest request) {
        Inspection inspection = inspectionService.updateInspection(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inspection report and assessment updated successfully", inspection));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelInspection(@PathVariable Long id) {
        inspectionService.cancelInspection(id);
        return ResponseEntity.ok(ApiResponse.success("Inspection request cancelled successfully", null));
    }
}
