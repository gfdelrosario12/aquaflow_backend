package com.aquaflow.backend.service;

import com.aquaflow.backend.domain.AwdConfigServiceImpl;
import com.aquaflow.backend.domain.AwdConfigValidator;
import com.aquaflow.backend.dto.request.AutoIrrigationConfigRequest;
import com.aquaflow.backend.dto.request.AwdThresholdConfigRequest;
import com.aquaflow.backend.dto.response.AutoIrrigationConfigResponse;
import com.aquaflow.backend.entity.AutoIrrigationConfig;
import com.aquaflow.backend.entity.CropGrowthStage;
import com.aquaflow.backend.entity.Field;
import com.aquaflow.backend.infrastructure.event.SystemEvent;
import com.aquaflow.backend.infrastructure.event.SystemEventPublisher;
import com.aquaflow.backend.infrastructure.exception.ResourceNotFoundException;
import com.aquaflow.backend.persistence.AutoIrrigationConfigRepository;
import com.aquaflow.backend.persistence.FieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AwdConfigServiceImplTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private AutoIrrigationConfigRepository autoIrrigationConfigRepository;

    @Mock
    private SystemEventPublisher systemEventPublisher;

    private AwdConfigValidator awdConfigValidator;
    private AwdConfigServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        awdConfigValidator = new AwdConfigValidator();
        service = new AwdConfigServiceImpl(
                fieldRepository,
                autoIrrigationConfigRepository,
                awdConfigValidator,
                systemEventPublisher
        );
    }

    @Test
    void shouldCreateDefaultConfigWhenGetConfigForNewField() {
        Field field = Field.builder().id(10L).name("North Field").build();

        when(fieldRepository.findById(10L)).thenReturn(Optional.of(field));
        when(autoIrrigationConfigRepository.findByFieldId(10L)).thenReturn(Optional.empty());
        when(autoIrrigationConfigRepository.save(any())).thenAnswer(invocation -> {
            AutoIrrigationConfig cfg = invocation.getArgument(0);
            cfg.setId(100L);
            return cfg;
        });

        AutoIrrigationConfigResponse response = service.getAwdConfig(10L);

        assertThat(response).isNotNull();
        assertThat(response.getFieldId()).isEqualTo(10L);
        assertThat(response.getConfigVersion()).isEqualTo(1L);
        assertThat(response.getEnabled()).isTrue();
        assertThat(response.getThresholds()).hasSize(4);
    }

    @Test
    void shouldUpdateConfigAndIncrementVersionAndPublishEvent() {
        Field field = Field.builder().id(10L).name("North Field").build();
        AutoIrrigationConfig existingConfig = AutoIrrigationConfig.builder()
                .id(100L)
                .field(field)
                .enabled(true)
                .maxDurationMinutes(60)
                .configVersion(1L)
                .thresholds(new ArrayList<>())
                .build();

        when(fieldRepository.findById(10L)).thenReturn(Optional.of(field));
        when(autoIrrigationConfigRepository.findByFieldId(10L)).thenReturn(Optional.of(existingConfig));
        when(autoIrrigationConfigRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AutoIrrigationConfigRequest updateRequest = AutoIrrigationConfigRequest.builder()
                .enabled(true)
                .maxDurationMinutes(180)
                .minCooldownMinutes(240)
                .allowedStartHour(7)
                .allowedEndHour(17)
                .targetFloodDepthCm(6.0)
                .rainDelayHours(12)
                .minConfidenceThreshold(0.90)
                .updatedBy("operator@aquaflow.io")
                .changeReason("Dry season policy adjustment")
                .thresholds(List.of(
                        AwdThresholdConfigRequest.builder()
                                .growthStage(CropGrowthStage.VEGETATIVE)
                                .triggerMoisturePercentage(45.0)
                                .targetMoisturePercentage(85.0)
                                .targetFloodDepthCm(6.0)
                                .build()
                ))
                .build();

        AutoIrrigationConfigResponse response = service.updateAwdConfig(10L, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getConfigVersion()).isEqualTo(2L); // Incremented!
        assertThat(response.getMaxDurationMinutes()).isEqualTo(180);
        assertThat(response.getUpdatedBy()).isEqualTo("operator@aquaflow.io");
        assertThat(response.getChangeReason()).isEqualTo("Dry season policy adjustment");
        assertThat(response.getThresholds()).hasSize(1);

        verify(systemEventPublisher).publish(any(SystemEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenFieldNotFound() {
        when(fieldRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAwdConfig(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

