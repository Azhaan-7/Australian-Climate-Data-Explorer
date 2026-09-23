package app;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class SaveSummaryHandler implements Handler {
    public static final String URL = "/save-summary";

    @Override
    public void handle(Context context) throws Exception {
        String flag = context.formParam("flags_drop");
        String metric = context.formParam("metric_drop");

        if (flag == null || metric == null) {
            context.result("Missing flag or metric for summary save.");
            return;
        }

        JDBCConnection jdbc = new JDBCConnection();
        String startDate = context.formParam("start-date");
        String endDate = context.formParam("end-date");
        String order = context.formParam("order");
        String ascdesc = context.formParam("ascdesc");

        // Retrieve current search results
        var results = jdbc.getSearch2CResults(flag, metric, startDate, endDate, order, ascdesc);

        jdbc.saveToSummary(flag, metric, results.size());

        // Redirect back to page2C
        context.redirect("/page2C.html");
    }
}