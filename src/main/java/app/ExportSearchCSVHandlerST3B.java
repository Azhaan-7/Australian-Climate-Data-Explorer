package app;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ExportSearchCSVHandlerST3B implements Handler {

    @Override
    public void handle(Context ctx) throws NumberFormatException, SQLException {
        JDBCConnection jdbc = new JDBCConnection();

        // Set Parameters for query
        String metricDrop = ctx.queryParam("metric");
        String numMetrics = ctx.queryParam("num_metrics");
        String startDate = ctx.queryParam("start-date");
        String endDate = ctx.queryParam("end-date");

        // Get search results
        List<SearchResult> results = jdbc.get3bResults(metricDrop, startDate, endDate, Integer.parseInt(numMetrics));

        // Set content type to CSV
        ctx.header("Content-Disposition", "attachment; filename=MetricSimilarities.csv");
        ctx.contentType("text/csv");

        // Build CSV string
        String csv = results.stream()
                .map(r -> r.metric + "," + r.avgPeriod1 + "," + r.avgPeriod2 + "," + r.percentChange + "," + r.diffFromRef)
                .collect(Collectors.joining("\n"));

        // Add header row
        csv = "Metric," + results.get(0).sumEarly + "-" + results.get(0).date + "," +
                results.get(0).date + "-" + results.get(0).sumLate + 
                ",PercentageChange,DifferenceFromReference\n" + csv;

        ctx.result(csv);
    }
}
