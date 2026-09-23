package app;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.List;
import java.util.stream.Collectors;

public class ExportSearchCSVHandlerST3A implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        JDBCConnection jdbc = new JDBCConnection();

        // Set Parameters for query
        String stationId = ctx.queryParam("station_id");
        String metric = ctx.queryParam("metric");
        String startDate = ctx.queryParam("start_date");
        String endDate = ctx.queryParam("end_date");
        String startDate1 = ctx.queryParam("start_date_1");
        String endDate1 = ctx.queryParam("end_date_1");
        String numStationsParam = ctx.queryParam("num_stations");

        int numStations=5;
        if (numStationsParam != null && !numStationsParam.isEmpty()) {
            numStations = Integer.parseInt(numStationsParam.trim()) + 1;
        }

        List<SearchResult> results = jdbc.getSimilarStations(
            stationId, metric, startDate, endDate, startDate1, endDate1, numStations
        );

        // Set content type to CSV
        ctx.header("Content-Disposition", "attachment; filename=SimilarStations.csv");
        ctx.contentType("text/csv");

        // Build CSV string
        String csv = results.stream()
                .map(r -> r.getStationId() + "," +
                          r.getName() + "," +
                          r.getAvgPeriod1() + "," +
                          r.getAvgPeriod2() + "," +
                          r.getPercentChange() + "," +
                          r.getDiffFromRef())
                .collect(Collectors.joining("\n"));

        // Add header row
        csv = "StationID,Station Name,Avg Period 1,Avg Period 2,% Change,Difference from Reference\n" + csv;

        ctx.result(csv);
    }
}
