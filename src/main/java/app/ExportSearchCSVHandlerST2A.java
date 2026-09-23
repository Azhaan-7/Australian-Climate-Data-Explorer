package app;

import io.javalin.http.Handler;
import io.javalin.http.Context;
import java.util.List;
import java.util.stream.Collectors;

public class ExportSearchCSVHandlerST2A implements Handler{
     @Override
    public void handle(Context ctx) {
        JDBCConnection jdbc = new JDBCConnection();

        // Set Parameters for query
        String metric = ctx.queryParam("metric");
        String state = ctx.queryParam("state");
        String startLat = ctx.queryParam("start-lat");
        String endLat = ctx.queryParam("end-lat");
        String order = ctx.queryParam("order");
        String ascdesc = ctx.queryParam("ascdesc");

        // Get search results
        List<SearchResult> results = jdbc.viewbystation(state,  startLat,  endLat, metric, order, ascdesc);

        // Set content type to CSV
        ctx.header("Content-Disposition", "attachment; filename=ViewByStation.csv");
        ctx.contentType("text/csv");

        // Build CSV string
        String csv = results.stream()
                .map(r -> r.stationId + "," + r.name + "," + r.metric + "," + r.lat)
                .collect(Collectors.joining("\n"));

        // Add header row
        csv = "StationID,Name,MetricValue,Latitude\n" + csv;

        ctx.result(csv);
    }
}
