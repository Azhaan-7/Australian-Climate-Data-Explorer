package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST2B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2B.html";

    @Override
    public void handle(Context context) throws Exception {
        JDBCConnection jdbc = new JDBCConnection();

        // Create a simple HTML webpage in a String
        String html = """
            <html>
                <head>
                    <title>View by Climate Metric</title>
                    <link rel='stylesheet' type='text/css' href='common.css'/>
                    <link rel='stylesheet' type='text/css' href='navbar.css'/>
                    <link rel='stylesheet' type='text/css' href='data_summary.css'/>
                    <link rel='stylesheet' type='text/css' href='buttons.css'/>
                    <link rel='stylesheet' type='text/css' href='drop_down.css'/>
                    <link rel='stylesheet' type='text/css' href='search_filters.css'/>
                </head>
                <body>
            """;
    
            html += """
                <div class='navbar'> <!-- NAVBAR DIV -->
                    <a href='/'><img class='logo' src='home.png'/></a>
                    <nav class='navbar-button'>
                        <ul>
                            <li><a href='/'>Homepage</a></li>    
                            <li><a href='mission.html'>Our Mission</a></li>
                            <li><a href='datasummary.html'>Understanding Our Data</a></li>
                            <li><a href='page2A.html'>View by Weather Station</a></li>
                            <li><a href='page2B.html'>View by Metric</a></li>
                            <li><a href='page2C.html'>View by Data Quality</a></li>
                            <li><a href='page3A.html'>View Station Similarities</a></li>
                            <li><a href='page3B.html'>View Metric Similarities</a></li>
                            <li><a href='page3C.html'>View Cross Metric Impact</a></li>
                        </ul>
                    </nav>
                </div>  <!-- END NAVBAR DIV -->
            """;

        // Add header content block
        html = html + """
            <div class='header'>
                <h1>View by Metric</h1>
            </div>
        """;

        html = html + "<div class='content'>";

        html = html + """
                <div class='page-description'>
                    <p><b>Welcome to the View Data By Metric Tool</b></p>
                    <p>Use this page to search climate data by selecting a <b>Climate Metric</b>, <b>Station ID Range</b>, and a <b>Date Range</b> using the filters below. You can then:</p>
                        <ul>
                            <li>Click <b>Search</b> to view matching records.</li>
                            <li>Click <b>Reset Filters</b> to clear your selections.</li>
                            <li><b>Export</b> your search results as a .csv file.</li>
                            <li><b>Save</b> a search to the <b>Summary Table</b> to track metric totals by state.</li>
                        </ul>
                    <p>As you save more results, the Summary Table will grow to reflect all searches made during your session. You can <b>export the Summary Table</b> or <b>clear it</b> to start over.</p>
                </div>            
        """;

        // Create drop-downs to filter search
        // Form Parameters
        String metricDrop = context.formParam("metrics_drop");
        String stationIdMin = context.formParam("station_id_min");
        String stationIdMax = context.formParam("station_id_max");
        String startDate = context.formParam("start-date");
        String endDate = context.formParam("end-date");
        
        ArrayList<String> metrics = jdbc.getMetrics();
        
        // Create drop-down element
        // metrics
        html = html + """
            <div>
                <form action ='/page2B.html' method='post'>
                    <div class='search_filters'>
                        <select id='metrics_drop' name='metrics_drop' class='drop-down'>
                        <option>--Climate Metric--</option>
        """;

        for (int i = 0; i < metrics.size(); ++i) {
            if (metrics.get(i).equals(metricDrop)){
                html = html + "<option selected>" + metrics.get(i) + "</option>";
            }
            else {
                html = html + "<option>" + metrics.get(i) + "</option>";
            }
        }

        html = html + "</select>";

        html += "<input type='number' placeholder='Min Station ID...' id='station_id_min' name='station_id_min' class='drop-down' value='" + 
               stationIdMin + "'><input type='number' placeholder='Max Station ID...' id='station_id_max' name='station_id_max' class='drop-down' value='" + 
               stationIdMax + "'>";

        // Calendar feature
        html = html + "<div class='date-selector'>" +
                        "<label for='start-date'>Start Date:</label>" +
                        "<input type='date' id='start-date' name='start-date' min='1970-01-01' max='2020-12-31' value='" + startDate + "'>" +
                        "<label for='end-date'>End Date:</label>" +
                        "<input type='date' id='end-date' name='end-date' min='1970-01-01' max='2020-12-31' value='" + endDate + "'>" +
                    "</div>";

        // Close search filters div
        html = html + "</div>";

        // End form
        html = html + """
                <div class='search-buttons'>
                    <button type='reset'>Reset Filters</button>
                    <button type='submit'>Search</button>
                </div>
            </form>
        </div>
        <div class='table'>
        """;

        boolean tableDisplayed = false;

        // Resolve search filters
        // Check for NULL values
        if (metricDrop != null && startDate != null && endDate != null && 
                stationIdMin != null && stationIdMax != null && !metricDrop.equals("--Climate Metric--")) {
            tableDisplayed = true;
            ArrayList<SearchResult> searchResults = jdbc.getSearch2BResults(metricDrop, stationIdMin, stationIdMax, startDate, endDate);

            html += "<div class='table-container'><table><thead><tr><th>Station Id</th><th>Date</th><th>" + metricDrop;
                  if (metricDrop.equals("Precipitation") || metricDrop.equals("Evaporation")) {
                    html += " (mm)";
                  }
                  else if (metricDrop.equals("MaxTemp") || metricDrop.equals("MinTemp")) {
                    html += " (Celsius)";
                  }
                  else if (metricDrop.contains("Humid")) {
                    html += " (%)";
                  }
                  else if (metricDrop.equals("Sunshine")) {
                    html += " (Hours)";
                  }
                  else if (metricDrop.contains("Okta")) {
                    html += " (oktas)";
                  }
            html += "</th></tr></thead><tbody>";

            for (SearchResult result : searchResults) {
                html += "<tr><td>" + result.getStationId() + "</td><td>" + result.getDate() + "</td><td>"
                        + result.getMetric() + "</td></tr>";
            }

            html += "</tbody></table></div>";
        } else {
            html = html + "<h3><i>Please select an option for each search filter</i></h3>";
        }

        if (tableDisplayed) {
            html += "<div class='search-table-btns'>";
            html += "<form action='/save-metric-summary' method='post'>";
            html += "<input type='hidden' name='metrics_drop' value='" + metricDrop + "'>";
            html += "<input type='hidden' name='station_id_min' value='" + stationIdMin + "'>";
            html += "<input type='hidden' name='station_id_max' value='" + stationIdMax + "'>";
            html += "<input type='hidden' name='start-date' value='" + startDate + "'>";
            html += "<input type='hidden' name='end-date' value='" + endDate + "'>";
            html += "<div><button type='submit' class='export-search-btn'>Save to Summary</button></div>";
            html += "</form>";
            html += "<form action='/export-search-2b' method='get'>" +
                        "<input type='hidden' name='metric' value='" + metricDrop + "'>" + 
                        "<input type='hidden' name='start-date' value='" + startDate + "'>" + 
                        "<input type='hidden' name='station_id_min' value='" + stationIdMin + "'>" +
                        "<input type='hidden' name='station_id_max' value='" + stationIdMax + "'>" +
                        "<input type='hidden' name='end-date' value='" + endDate + "'>" + 
                        "<button type='submit' class='export-search-btn'>Export Search Table</button>" + 
                    "</form>" +
                    "</div>";
        }

        ArrayList<MetricSummaryRow> summaryRows = jdbc.getMetricSummaryRows();
        if (!summaryRows.isEmpty()) {
            html += "<div class='table'><h2>Summary Table</h2><div class='table-container'><table><thead><tr><th>State</th>";

            MetricSummaryRow firstRow = summaryRows.get(0);
            for (String metric : firstRow.metricTotal.keySet()) {
                html += "<th>" + metric + "</th>";
            }
            html += "</tr></thead><tbody>";

            for (MetricSummaryRow row : summaryRows) {
                html += "<tr><td>" + row.state + "</td>";
                for (String metric : firstRow.metricTotal.keySet()) {
                    String val = row.metricTotal.get(metric);
                    html += "<td>" + (val != null ? val : "null") + "</td>";
                }
                html += "</tr>";
            }

            html += "</tbody></table></div></div>";
            html += """
                <div class='clear-summary-buttons'>
                    <form action='/clear-metric-summary' method='post'>
                        <div>
                            <button type='submit' class='clear-btn'>Clear Summary Table</button>
                        </div>
                    </form>
                    <form action="/export-summary-2b" method="get">
                        <div>
                            <button type="submit" class='export-summary-btn'>Export Summary Table</button>
                        </div>
                    </form>
                </div>
                    """;
        }
        
        // Close Content div
        html = html + "</div>";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }
}
