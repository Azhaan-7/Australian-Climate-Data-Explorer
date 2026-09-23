package app;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.List;
import java.util.stream.Collectors;

public class ExportSearchTableHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        // Get filters from the form
        String state = ctx.queryParam("states_drop");
        String metric = ctx.queryParam("metrics_drop");
        String startLat = ctx.queryParam("start_lat");
        String endLat = ctx.queryParam("end_lat");
        String order = ctx.queryParam("order");
        String ascdesc = ctx.queryParam("ascdesc");

        if (state == null || metric == null || startLat == null || endLat == null) {
            ctx.result("Missing search parameters.");
            return;
        }

        JDBCConnection jdbc = new JDBCConnection();
        List<SearchResult> results = jdbc.viewbystation(state, startLat, endLat, metric, order, ascdesc);

        // Set headers
        ctx.header("Content-Disposition", "attachment; filename=SearchResults.csv");
        ctx.header("Content-Type", "text/csv");

        // Build CSV
        String csv = results.stream()
            .map(r -> r.stationId + "," + r.name + "," + r.metric + "," + r.lat + "," + r.region)
            .collect(Collectors.joining("\n"));

        csv = "StationID,StationName," + metric + ",Latitude,Region\n" + csv;


        ctx.result(csv);
    }
}
