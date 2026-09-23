package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.ZoneRequest;
import com.aquaflow.backend.dto.response.ZoneResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ZoneService {
    ZoneResponse createZone(ZoneRequest request);
    ZoneResponse getZoneById(Long id);
    Page<ZoneResponse> getAllZones(Pageable pageable);
    ZoneResponse updateZone(Long id, ZoneRequest request);
    void deleteZone(Long id);
    List<ZoneResponse> getZonesByCropType(String cropType);
    List<ZoneResponse> getAllZones();
}