package app;

import java.util.HashMap;

// Helper class to retrieve Rows
public class SummaryRow {
    public String flag;
    public HashMap<String, String> metricCounts = new HashMap<>();

    public SummaryRow(String flag) {
        this.flag = flag;
    }
}
