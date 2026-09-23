package com.aquaflow.backend.infrastructure.util;

import com.aquaflow.backend.dto.response.*;
import com.aquaflow.backend.entity.*;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public static ZoneResponse toZoneResponse(Zone zone) {
        return ZoneResponse.builder()
                .id(zone.getId())
                .name(zone.getName())
                .area(zone.getArea())
                .cropType(zone.getCropType())
                .waterAllocationLimit(zone.getWaterAllocationLimit())
                .createdAt(zone.getCreatedAt())
                .updatedAt(zone.getUpdatedAt())
                .build();
    }

    public static CropResponse toCropResponse(Crop crop) {
        return CropResponse.builder()
                .id(crop.getId())
                .name(crop.getName())
                .waterPerStage(crop.getWaterPerStage())
                .growingSeasonDays(crop.getGrowingSeasonDays())
                .optimalTemperatureMin(crop.getOptimalTemperatureMin())
                .optimalTemperatureMax(crop.getOptimalTemperatureMax())
                .createdAt(crop.getCreatedAt())
                .updatedAt(crop.getUpdatedAt())
                .build();
    }

    public static IrrigationScheduleResponse toScheduleResponse(IrrigationSchedule schedule) {
        return IrrigationScheduleResponse.builder()
                .id(schedule.getId())
                .zoneId(schedule.getZone().getId())
                .startTime(schedule.getStartTime())
                .duration(schedule.getDuration())
                .waterVolume(schedule.getWaterVolume())
                .recurrenceRule(schedule.getRecurrenceRule())
                .status(schedule.getStatus())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

    public static DeviceResponse toDeviceResponse(Device device) {
        return DeviceResponse.builder()
                .deviceId(device.getDeviceId())
                .hardwareModel(device.getHardwareModel())
                .firmwareVersion(device.getFirmwareVersion())
                .targetFirmwareVersion(device.getTargetFirmwareVersion())
                .status(device.getStatus())
                .lastHeartbeat(device.getLastHeartbeat())
                .associatedZones(device.getAssociatedZones())
                .createdAt(device.getCreatedAt())
                .updatedAt(device.getUpdatedAt())
                .build();
    }

    public static SensorDataResponse toSensorDataResponse(SensorReading reading) {
        return SensorDataResponse.builder()
                .id(reading.getId())
                .deviceId(reading.getDeviceId())
                .sensorType(reading.getSensorType() != null ? reading.getSensorType().name() : null)
                .value(reading.getValue())
                .unit(reading.getUnit())
                .timestamp(reading.getTimestamp())
                .zoneId(reading.getZoneId())
                .createdAt(reading.getCreatedAt())
                .build();
    }

    public static AlertResponse toAlertResponse(Alert alert) {
        return AlertResponse.builder()
                .id(alert.getId())
                .deviceId(alert.getDeviceId())
                .zoneId(alert.getZoneId())
                .alertType(alert.getAlertType())
                .alertLevel(alert.getAlertLevel())
                .message(alert.getMessage())
                .acknowledged(alert.isAcknowledged())
                .createdAt(alert.getCreatedAt())
                .acknowledgedAt(alert.getAcknowledgedAt())
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static FieldResponse toFieldResponse(Field field) {
        if (field == null) return null;
        return FieldResponse.builder()
                .id(field.getId())
                .name(field.getName())
                .boundaryGeoJson(field.getBoundaryGeoJson())
                .areaHectares(field.getAreaHectares())
                .createdAt(field.getCreatedAt())
                .updatedAt(field.getUpdatedAt())
                .build();
    }

    public static MonitoringZoneResponse toMonitoringZoneResponse(MonitoringZone zone) {
        if (zone == null) return null;
        return MonitoringZoneResponse.builder()
                .id(zone.getId())
                .name(zone.getName())
                .fieldId(zone.getField() != null ? zone.getField().getId() : null)
                .cropType(zone.getCropType())
                .targetMoisturePercentage(zone.getTargetMoisturePercentage())
                .waterAllocationLimitLiters(zone.getWaterAllocationLimitLiters())
                .createdAt(zone.getCreatedAt())
                .updatedAt(zone.getUpdatedAt())
                .build();
    }

    public static EdgeNodeResponse toEdgeNodeResponse(EdgeNode node) {
        if (node == null) return null;
        return EdgeNodeResponse.builder()
                .id(node.getId())
                .nodeId(node.getNodeId())
                .identityType(node.getIdentity() != null && node.getIdentity().getIdentityType() != null ? node.getIdentity().getIdentityType().name() : null)
                .identityValue(node.getIdentity() != null ? node.getIdentity().getIdentityValue() : null)
                .macAddress(node.getIdentity() != null ? node.getIdentity().getMacAddress() : null)
                .serialNumber(node.getIdentity() != null ? node.getIdentity().getSerialNumber() : null)
                .lifecycleState(node.getLifecycleState())
                .healthState(node.getHealthState())
                .batteryLevel(node.getHealthMetrics() != null ? node.getHealthMetrics().getBatteryLevel() : null)
                .solarVoltage(node.getHealthMetrics() != null ? node.getHealthMetrics().getSolarVoltage() : null)
                .signalDbm(node.getHealthMetrics() != null ? node.getHealthMetrics().getSignalDbm() : null)
                .lastHeartbeat(node.getHealthMetrics() != null ? node.getHealthMetrics().getLastHeartbeat() : null)
                .monitoringZoneId(node.getMonitoringZone() != null ? node.getMonitoringZone().getId() : null)
                .hardwareModel(node.getHardwareModel())
                .firmwareVersion(node.getFirmwareVersion())
                .version(node.getVersion())
                .createdAt(node.getCreatedAt())
                .updatedAt(node.getUpdatedAt())
                .build();
    }

    public static MonitoringPointResponse toMonitoringPointResponse(MonitoringPoint point) {
        if (point == null) return null;
        return MonitoringPointResponse.builder()
                .id(point.getId())
                .name(point.getName())
                .monitoringZoneId(point.getMonitoringZone() != null ? point.getMonitoringZone().getId() : null)
                .edgeNodeId(point.getEdgeNode() != null ? point.getEdgeNode().getId() : null)
                .primarySensorType(point.getPrimarySensorType())
                .depthCm(point.getDepthCm())
                .latitude(point.getLatitude())
                .longitude(point.getLongitude())
                .createdAt(point.getCreatedAt())
                .updatedAt(point.getUpdatedAt())
                .build();
    }

    public static TelemetryReadingResponse toTelemetryReadingResponse(TelemetryReading reading) {
        if (reading == null) return null;
        return TelemetryReadingResponse.builder()
                .id(reading.getId())
                .monitoringPointId(reading.getMonitoringPoint() != null ? reading.getMonitoringPoint().getId() : null)
                .edgeNodeId(reading.getEdgeNode() != null ? reading.getEdgeNode().getId() : null)
                .sensorType(reading.getSensorType())
                .valueNum(reading.getValueNum())
                .unit(reading.getUnit())
                .timestamp(reading.getTimestamp())
                .createdAt(reading.getCreatedAt())
                .build();
    }

    public static IrrigationDecisionResponse toIrrigationDecisionResponse(IrrigationDecision decision) {
        if (decision == null) return null;
        return IrrigationDecisionResponse.builder()
                .id(decision.getId())
                .edgeNodeId(decision.getEdgeNode() != null ? decision.getEdgeNode().getId() : null)
                .decisionType(decision.getDecisionType())
                .triggerReason(decision.getTriggerReason())
                .requestedDurationMinutes(decision.getRequestedDurationMinutes())
                .requestedVolumeLiters(decision.getRequestedVolumeLiters())
                .executionStatus(decision.getExecutionStatus())
                .nodeTimestamp(decision.getNodeTimestamp())
                .createdAt(decision.getCreatedAt())
                .build();
    }

    public static AutoIrrigationConfigResponse toAutoIrrigationConfigResponse(AutoIrrigationConfig config) {
        if (config == null) return null;
        return AutoIrrigationConfigResponse.builder()
                .id(config.getId())
                .edgeNodeId(config.getEdgeNode() != null ? config.getEdgeNode().getId() : null)
                .scheduleMode(config.getScheduleMode())
                .minSoilMoisturePercentage(config.getMinSoilMoisturePercentage())
                .maxSoilMoisturePercentage(config.getMaxSoilMoisturePercentage())
                .maxSingleRunMinutes(config.getMaxSingleRunMinutes())
                .safetyRainOverride(config.getSafetyRainOverride())
                .cronSchedule(config.getCronSchedule())
                .version(config.getVersion())
                .createdAt(config.getCreatedAt())
                .updatedAt(config.getUpdatedAt())
                .build();
    }

    public static IrrigationAuditLogResponse toIrrigationAuditLogResponse(IrrigationAuditLog log) {
        if (log == null) return null;
        return IrrigationAuditLogResponse.builder()
                .id(log.getId())
                .eventType(log.getEventType())
                .actor(log.getActor())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .payloadJson(log.getPayloadJson())
                .createdAt(log.getCreatedAt())
                .build();
    }
}