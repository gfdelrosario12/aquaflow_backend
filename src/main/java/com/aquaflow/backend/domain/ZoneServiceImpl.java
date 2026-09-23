package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.ZoneRequest;
import com.aquaflow.backend.dto.response.ZoneResponse;
import com.aquaflow.backend.entity.Zone;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.ZoneRepository;
import com.aquaflow.backend.domain.ZoneService;
import com.aquaflow.backend.infrastructure.util.DtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ZoneServiceImpl implements ZoneService {

    private static final Logger log = LoggerFactory.getLogger(ZoneServiceImpl.class);

    private final ZoneRepository zoneRepository;

    public ZoneServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public ZoneResponse createZone(ZoneRequest request) {
        log.info("Creating zone: {}", request.getName());

        if (zoneRepository.existsByName(request.getName())) {
            throw new ValidationException("Zone with name '" + request.getName() + "' already exists", "ZONE_DUPLICATE");
        }

        Zone zone = Zone.builder()
                .name(request.getName())
                .area(request.getArea())
                .cropType(request.getCropType())
                .waterAllocationLimit(request.getWaterAllocationLimit())
                .build();

        Zone saved = zoneRepository.save(zone);
        log.info("Zone created with id: {}", saved.getId());
        return DtoMapper.toZoneResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ZoneResponse getZoneById(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + id));
        return DtoMapper.toZoneResponse(zone);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ZoneResponse> getAllZones(Pageable pageable) {
        return zoneRepository.findAll(pageable).map(DtoMapper::toZoneResponse);
    }

    @Override
    public List<ZoneResponse> getAllZones() {
        return zoneRepository.findAll().stream()
                .map(DtoMapper::toZoneResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ZoneResponse updateZone(Long id, ZoneRequest request) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + id));

        zone.setName(request.getName());
        zone.setArea(request.getArea());
        zone.setCropType(request.getCropType());
        zone.setWaterAllocationLimit(request.getWaterAllocationLimit());

        Zone saved = zoneRepository.save(zone);
        log.info("Zone updated with id: {}", id);
        return DtoMapper.toZoneResponse(saved);
    }

    @Override
    public void deleteZone(Long id) {
        if (!zoneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Zone not found with id: " + id);
        }
        zoneRepository.deleteById(id);
        log.info("Zone deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ZoneResponse> getZonesByCropType(String cropType) {
        return zoneRepository.findByCropType(cropType).stream()
                .map(DtoMapper::toZoneResponse)
                .collect(Collectors.toList());
    }
}