package com.aquaflow.backend.domain;

import com.aquaflow.backend.dto.request.CropRequest;
import com.aquaflow.backend.dto.response.CropResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CropService {
    CropResponse createCrop(CropRequest request);
    CropResponse getCropById(Long id);
    Page<CropResponse> getAllCrops(Pageable pageable);
    CropResponse updateCrop(Long id, CropRequest request);
    void deleteCrop(Long id);
    List<CropResponse> getAllCrops();
}