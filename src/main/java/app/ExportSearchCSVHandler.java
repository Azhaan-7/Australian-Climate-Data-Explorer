package app;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.List;
import java.util.stream.Collectors;

public class ExportSearchCSVHandler implements Handler {

    @Override
    public void handle(Context ctx) {
        JDBCConnection jdbc = new JDBCConnection();

        // Set Parameters for query
        String metric = ctx.queryParam("metric");
        String flag = ctx.queryParam("flag");
        String start = ctx.queryParam("start-date");
        String end = ctx.queryParam("end-date");
        String order = ctx.queryParam("order");
        String ascdesc = ctx.queryParam("ascdesc");

        // Get search results
        List<SearchResult> results = jdbc.getSearch2CResults(flag, metric, start, end, order, ascdesc);

        // Set content type to CSV
        ctx.header("Content-Disposition", "attachment; filename=DataQuality.csv");
        ctx.contentType("text/csv");

        // Build CSV string
        String csv = results.stream()
                .map(r -> r.stationId + "," + r.date + "," + r.metric + "," + r.quality)
                .collect(Collectors.joining("\n"));

        // Add header row
        csv = "StationID,Date,MetricValue,QualityFlag\n" + csv;

        ctx.result(csv);
    }
}
