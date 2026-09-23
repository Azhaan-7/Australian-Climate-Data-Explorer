package app;

import java.util.HashMap;

// Helper class to retrieve Rows
public class MetricSummaryRow {
    public String state;
    public HashMap<String, String> metricTotal = new HashMap<>();

    public MetricSummaryRow(String state) {
        this.state = state;
    }
}
