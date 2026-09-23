package app;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class SaveMetricSummaryHandler implements Handler {
    public static final String URL = "/save-metric-summary";

    @Override
    public void handle(Context context) throws Exception {
        String metric = context.formParam("metrics_drop");
        String stationIdMin = context.formParam("station_id_min");
        String stationIdMax = context.formParam("station_id_max");
        String startDate = context.formParam("start-date");
        String endDate = context.formParam("end-date");

        if (stationIdMin == null || stationIdMax == null || metric == null || startDate == null || endDate == null) {
            context.result("Missing filters for summary save.");
            return;
        }

        JDBCConnection jdbc = new JDBCConnection();

        // Retrieve current search results
        var results = jdbc.getSearch2BResults(metric, stationIdMin, stationIdMax, startDate, endDate);

        jdbc.saveMetricToSummary(metric, stationIdMin, stationIdMax, startDate, endDate, results);

        // Redirect back to page2B
        context.redirect("/page2B.html");
    }
}