package app;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;

/**
 * Main Application Class.
 * <p>
 * Running this class as regular java application will start the
 * Javalin HTTP Server and our web application.
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class App {

    public static final int DEFAULT_PORT = 7001;
    public static final String CSS_DIR = "css/";
    public static final String IMAGES_DIR = "images/";

    public static void main(String[] args) {
        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", String.valueOf(DEFAULT_PORT)));
        // Create the HTTP server using the deployment port or the local default.
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/help/routes"));

            // Uncomment this if you have files in the CSS Directory
            config.addStaticFiles(CSS_DIR);

            // Uncomment this if you have files in the Images Directory
            config.addStaticFiles(IMAGES_DIR);
       }).start("0.0.0.0", port);

        // Configure Web Routes
        configureRoutes(app);
    }

    public static void configureRoutes(Javalin app) {
        // All webpages are listed here as GET pages
        app.get(PageIndex.URL, new PageIndex());
        app.get(PageMission.URL, new PageMission());
        app.get(PageDataSummary.URL, new PageDataSummary());
        app.get(PageST2A.URL, new PageST2A());
        app.get(PageST2B.URL, new PageST2B());
        app.get(PageST2C.URL, new PageST2C());
        app.get(PageST3A.URL, new PageST3A());
        app.get(PageST3B.URL, new PageST3B());
        app.get(PageST3C.URL, new PageST3C());
        app.get(SaveSummaryHandler.URL, new SaveSummaryHandler());
        app.get(SaveMetricSummaryHandler.URL, new SaveMetricSummaryHandler());
        app.get(SaveRegionSummaryHandler.URL, new SaveRegionSummaryHandler());

        app.get("/export-search-2b", new ExportSearchCSVHandlerST2B());
        app.get("/export-summary-2b", ctx -> {
            JDBCConnection jdbc = new JDBCConnection();
            String csvContent = jdbc.getSummary2BAsCSV();

            ctx.header("Content-Disposition", "attachment; filename=MetricSummary.csv");
            ctx.header("Content-Type", "text/csv");
            ctx.result(csvContent);
        });
        app.get("/export-search-3b", new ExportSearchCSVHandlerST3B());

        app.get("/export-search", new ExportSearchCSVHandler());
        app.get("/export-station-search", new ExportSearchTableHandler());
        app.get("/export-summary", ctx -> {
            JDBCConnection jdbc = new JDBCConnection();
            String csvContent = jdbc.getSummaryAsCSV();

            ctx.header("Content-Disposition", "attachment; filename=QualitySummary.csv");
            ctx.header("Content-Type", "text/csv");
            ctx.result(csvContent);
        });
        app.get("/export-region-summary", ctx -> {
            JDBCConnection jdbc = new JDBCConnection();
            String csvContent = jdbc.getRegionSummaryAsCSV();

            ctx.header("Content-Disposition", "attachment; filename=RegionSummary.csv");
            ctx.header("Content-Type", "text/csv");
            ctx.result(csvContent);
        });
        app.get("/export-st3a", new ExportSearchCSVHandlerST3A());

        app.get("/export-search-3C", new ExportSearchCSVHandlerST3C());

        // Add / uncomment POST commands for any pages that need web form POSTS
        app.post(PageIndex.URL, new PageIndex());
        app.post(PageMission.URL, new PageMission());
        app.post(PageDataSummary.URL, new PageDataSummary());
        app.post(PageST2A.URL, new PageST2A());
        app.post(PageST2B.URL, new PageST2B());
        app.post(PageST2C.URL, new PageST2C());
        app.post(PageST3A.URL, new PageST3A());
        app.post(PageST3B.URL, new PageST3B());
        app.post(PageST3C.URL, new PageST3C());
        app.post(SaveSummaryHandler.URL, new SaveSummaryHandler());
        app.post("/clear-summary", ctx -> {
            JDBCConnection jdbc = new JDBCConnection();
            jdbc.clearSummaryTable();
            ctx.redirect("/page2C.html");
        });
        app.post(SaveRegionSummaryHandler.URL, new SaveRegionSummaryHandler());
        app.post("/clear-region-summary", ctx -> {
            JDBCConnection jdbc = new JDBCConnection();
            jdbc.clearRegionSummaryTable();
            ctx.redirect("/page2A.html");
        });
        app.post(SaveMetricSummaryHandler.URL, new SaveMetricSummaryHandler());
        app.post("/clear-metric-summary", ctx -> {
            JDBCConnection jdbc = new JDBCConnection();
            jdbc.clearMetricSummaryTable();
            ctx.redirect("/page2B.html");
        });

    }

}
