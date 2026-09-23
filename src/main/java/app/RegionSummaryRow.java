package app;

import java.util.HashMap;

public class RegionSummaryRow {
    public String region;
    public HashMap<String, String> metricStats = new HashMap<>();

    public RegionSummaryRow(String region) {
        this.region = region;
    }
}
