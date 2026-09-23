package app;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.*;

public class SaveRegionSummaryHandler implements Handler {
    public static final String URL = "/save-region-summary";

    @Override
    public void handle(Context context) throws Exception {
        String state = context.formParam("states_drop");
        String metric = context.formParam("metrics_drop");
        String startLat = context.formParam("start_lat");
        String endLat = context.formParam("end_lat");
        String order = context.formParam("order");
        String ascdesc = context.formParam("ascdesc");
        

        if (state == null || metric == null || startLat == null || endLat == null) {
            context.result("Missing filters for saving summary.");
            return;
        }

        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<SearchResult> results = jdbc.viewbystation(state, startLat, endLat, metric,order,ascdesc);

        
        Map<String, List<Double>> regionToMetricValues = new HashMap<>();
        Map<String, Set<String>> regionToStations = new HashMap<>();

        
        for (SearchResult result : results) {
            if (result.region == null || result.metric == null || result.metric.isBlank()) continue;

            try {
                double val = Double.parseDouble(result.metric);

                regionToMetricValues
                        .computeIfAbsent(result.region, k -> new ArrayList<>())
                        .add(val);

                regionToStations
                        .computeIfAbsent(result.region, k -> new HashSet<>())
                        .add(result.stationId);

            } catch (NumberFormatException ignored) {
                // Ignore invalid metric values
            }
        }

        // ✅ Save summary per region
        for (Map.Entry<String, List<Double>> entry : regionToMetricValues.entrySet()) {
            String region = entry.getKey();
            List<Double> values = entry.getValue();

            double avg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            int stationCount = regionToStations.getOrDefault(region, Set.of()).size();

            jdbc.saveToRegionSummary(region, metric, avg, stationCount);
        }

        // ✅ Go back to your page
        context.redirect("/page2A.html");
    }
}
