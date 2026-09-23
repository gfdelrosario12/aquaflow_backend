package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.UpdateAwdProfileRequest;
import com.aquaflow.backend.dto.request.UpdateCropStageRequest;
import com.aquaflow.backend.dto.request.ZoneRequest;
import com.aquaflow.backend.dto.response.MonitoringZoneResponse;
import com.aquaflow.backend.dto.response.ZoneResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ZoneService {
    ZoneResponse createZone(ZoneRequest request);
    ZoneResponse getZoneById(Long id);
    MonitoringZoneResponse getMonitoringZoneById(Long id);
    Page<ZoneResponse> getAllZones(Pageable pageable);
    ZoneResponse updateZone(Long id, ZoneRequest request);
    MonitoringZoneResponse updateCropStage(Long id, UpdateCropStageRequest request);
    MonitoringZoneResponse updateAwdProfile(Long id, UpdateAwdProfileRequest request);
    void deleteZone(Long id);
    List<ZoneResponse> getZonesByCropType(String cropType);
    List<ZoneResponse> getAllZones();
}