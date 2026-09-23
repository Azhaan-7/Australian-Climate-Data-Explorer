package app;

import java.util.List;
import java.util.stream.Collectors;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ExportSearchCSVHandlerST2B implements Handler {

    @Override
    public void handle(Context ctx) {
        JDBCConnection jdbc = new JDBCConnection();

        // Set Parameters for query
        String metric = ctx.queryParam("metric");
        String start = ctx.queryParam("start-date");
        String end = ctx.queryParam("end-date");
        String stationIdMin = ctx.queryParam("station_id_min");
        String stationIdMax = ctx.queryParam("station_id_max");

        // Get search results
        List<SearchResult> results = jdbc.getSearch2BResults(metric, stationIdMin, stationIdMax, start, end);

        // Set content type to CSV
        ctx.header("Content-Disposition", "attachment; filename=ClimateMetric.csv");
        ctx.contentType("text/csv");

        // Build CSV string
        String csv = results.stream()
                .map(r -> r.stationId + "," + r.date + "," + r.metric)
                .collect(Collectors.joining("\n"));


        // Add header row
        csv = "StationID,Date,MetricValue\n" + csv;

        ctx.result(csv);
    }
}
