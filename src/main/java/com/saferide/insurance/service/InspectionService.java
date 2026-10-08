package com.saferide.insurance.service;

import com.saferide.insurance.dto.request.InspectionRequest;
import com.saferide.insurance.entity.inspection.Inspection;

import java.util.List;

public interface InspectionService {
    Inspection createInspection(InspectionRequest request);
    Inspection getInspectionById(Long id);
    Inspection getInspectionByReference(String inspectionReference);
    List<Inspection> getInspectionsByVehicleId(Long vehicleId);
    List<Inspection> getAllInspections();
    Inspection updateInspection(Long id, InspectionRequest request);
    void cancelInspection(Long id);
}
