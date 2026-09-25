package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.CropRequest;
import com.aquaflow.backend.dto.response.CropResponse;
import com.aquaflow.backend.entity.Crop;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.infrastructure.exception.ValidationException;
import com.aquaflow.backend.persistence.CropRepository;
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
public class CropServiceImpl implements CropService {

    private static final Logger log = LoggerFactory.getLogger(CropServiceImpl.class);

    private final CropRepository cropRepository;

    public CropServiceImpl(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    @Override
    public CropResponse createCrop(CropRequest request) {
        log.info("Creating crop: {}", request.getName());

        if (cropRepository.existsByName(request.getName())) {
            throw new ValidationException("Crop with name '" + request.getName() + "' already exists", "CROP_DUPLICATE");
        }

        Crop crop = Crop.builder()
                .name(request.getName())
                .waterPerStage(request.getWaterPerStage())
                .growingSeasonDays(request.getGrowingSeasonDays())
                .optimalTemperatureMin(request.getOptimalTemperatureMin())
                .optimalTemperatureMax(request.getOptimalTemperatureMax())
                .build();

        Crop saved = cropRepository.save(crop);
        log.info("Crop created with id: {}", saved.getId());
        return DtoMapper.toCropResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CropResponse getCropById(Long id) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + id));
        return DtoMapper.toCropResponse(crop);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CropResponse> getAllCrops(Pageable pageable) {
        return cropRepository.findAll(pageable).map(DtoMapper::toCropResponse);
    }

    @Override
    public List<CropResponse> getAllCrops() {
        return cropRepository.findAll().stream()
                .map(DtoMapper::toCropResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CropResponse updateCrop(Long id, CropRequest request) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with id: " + id));

        crop.setName(request.getName());
        crop.setWaterPerStage(request.getWaterPerStage());
        crop.setGrowingSeasonDays(request.getGrowingSeasonDays());
        crop.setOptimalTemperatureMin(request.getOptimalTemperatureMin());
        crop.setOptimalTemperatureMax(request.getOptimalTemperatureMax());

        Crop saved = cropRepository.save(crop);
        log.info("Crop updated with id: {}", id);
        return DtoMapper.toCropResponse(saved);
    }

    @Override
    public void deleteCrop(Long id) {
        if (!cropRepository.existsById(id)) {
            throw new ResourceNotFoundException("Crop not found with id: " + id);
        }
        cropRepository.deleteById(id);
        log.info("Crop deleted with id: {}", id);
    }
}