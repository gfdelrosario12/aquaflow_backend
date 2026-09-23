package com.aquaflow.backend.api;

import com.aquaflow.backend.dto.request.IrrigationScheduleRequest;
import com.aquaflow.backend.dto.response.IrrigationScheduleResponse;
import com.aquaflow.backend.domain.IrrigationScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
public class IrrigationScheduleController {

    private static final Logger log = LoggerFactory.getLogger(IrrigationScheduleController.class);

    private final IrrigationScheduleService scheduleService;

    public IrrigationScheduleController(IrrigationScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<IrrigationScheduleResponse> createSchedule(@RequestBody IrrigationScheduleRequest request) {
        log.info("POST /api/v1/schedules");
        IrrigationScheduleResponse response = scheduleService.createSchedule(request);
        return ResponseEntity.created(URI.create("/api/v1/schedules/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IrrigationScheduleResponse> getScheduleById(@PathVariable Long id) {
        log.info("GET /api/v1/schedules/{}", id);
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @GetMapping
    public ResponseEntity<Page<IrrigationScheduleResponse>> getAllSchedules(Pageable pageable) {
        log.info("GET /api/v1/schedules");
        return ResponseEntity.ok(scheduleService.getAllSchedules(pageable));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<IrrigationScheduleResponse>> getSchedulesByZoneId(@PathVariable Long zoneId) {
        log.info("GET /api/v1/schedules/zone/{}", zoneId);
        return ResponseEntity.ok(scheduleService.getSchedulesByZoneId(zoneId));
    }

    @GetMapping("/zone/{zoneId}/ordered")
    public ResponseEntity<List<IrrigationScheduleResponse>> getSchedulesByZoneIdOrdered(@PathVariable Long zoneId) {
        log.info("GET /api/v1/schedules/zone/{}/ordered", zoneId);
        return ResponseEntity.ok(scheduleService.getSchedulesByZoneIdOrderByStartTimeAsc(zoneId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IrrigationScheduleResponse> updateSchedule(@PathVariable Long id, @RequestBody IrrigationScheduleRequest request) {
        log.info("PUT /api/v1/schedules/{}", id);
        return ResponseEntity.ok(scheduleService.updateSchedule(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        log.info("DELETE /api/v1/schedules/{}", id);
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}