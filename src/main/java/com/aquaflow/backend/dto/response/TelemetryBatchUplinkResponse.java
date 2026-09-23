package com.aquaflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryBatchUplinkResponse {

    private int totalProcessed;
    private int successful;
    private int failed;
    private int duplicates;
    private List<TelemetryUplinkResponse> results;

    public int getTotalProcessed() { return totalProcessed; }
    public void setTotalProcessed(int totalProcessed) { this.totalProcessed = totalProcessed; }
    public int getSuccessful() { return successful; }
    public void setSuccessful(int successful) { this.successful = successful; }
    public int getFailed() { return failed; }
    public void setFailed(int failed) { this.failed = failed; }
    public int getDuplicates() { return duplicates; }
    public void setDuplicates(int duplicates) { this.duplicates = duplicates; }
    public List<TelemetryUplinkResponse> getResults() { return results; }
    public void setResults(List<TelemetryUplinkResponse> results) { this.results = results; }
}

