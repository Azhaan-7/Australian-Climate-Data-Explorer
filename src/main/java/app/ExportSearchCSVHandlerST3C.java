package app;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class ExportSearchCSVHandlerST3C implements Handler {

    @Override
    public void handle(Context ctx) {
        JDBCConnection jdbc = new JDBCConnection();

        // Set Parameters for query
        String metric = ctx.queryParam("metric");
        String station = ctx.queryParam("station");
        String start = ctx.queryParam("start-date");
        String end = ctx.queryParam("end-date");

        // Get search results
        List<SearchResult> results = jdbc.getSearch3CResults(station, metric, start, end);

        // Parse Dates
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        LocalDate midPoint = startDate.plusDays(daysBetween / 2);

        // Set content type to CSV
        ctx.header("Content-Disposition", "attachment; filename=DataCorrelation.csv");
        ctx.contentType("text/csv");

        // Build CSV string
        String csv = results.stream()
                .map(r -> r.metric + "," + r.sumEarly + "," + r.sumLate + "," + r.percentChange)
                .collect(Collectors.joining("\n"));

        // Add header row
        csv = "Climate Metric,Total ("+startDate.getYear()+"-"+midPoint.getYear()+"),Total ("+midPoint.getYear()+"-"+endDate.getYear()+"),Change (%)\n" + csv;

        ctx.result(csv);
    }
}
