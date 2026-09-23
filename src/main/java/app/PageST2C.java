package app;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
public class PageST2C implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2C.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";
        JDBCConnection jdbc = new JDBCConnection();

        // Add some Head information
        html = html + "<head>" + 
               "<title>Quality of Climate Data</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='mission.css' />";
        html = html + "<link rel='stylesheet' type='text/css' href='buttons.css'/>";
        html = html + "<link rel='stylesheet' type='text/css' href='search_filters.css'/>";
        html = html + "<link rel='stylesheet' type='text/css' href='navbar.css'/>";
        html = html + "<link rel='stylesheet' type='text/css' href='common.css'/>";
        html = html + "</head>";

        // Add the body
        html = html + "<body>";

        // Add the topnav
        // This uses a Java v15+ Text Block
        html = html + """
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
                <h1>Quality of Climate Data</h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
                <div class='page-description'>
                    <p><b>Welcome to the Data Quality & Climate Metric Search Tool</b></p>
                    <p>Use this page to search climate data by selecting a <b>Data Quality Flag</b>, <b>Climate Metric</b>, and a <b>date range</b> using the filters below. You can then:</p>
                        <ul>
                            <li>Select a <b>Sort Table</b> option and a <b>Sort Direction</b> to customize the presentation of data.</li>
                            <li>Click <b>Search</b> to view matching records.</li>
                            <li>Click <b>Reset Filters</b> to clear your selections.</li>
                            <li><b>Export</b> your search results as a .csv file.</li>
                            <li><b>Save</b> a search to the <b>Summary Table</b> to track how many records match each flag and metric.</li>
                        </ul>
                    <p>As you save more results, the Summary Table will grow to reflect all searches made during your session. You can <b>export the Summary Table</b> or <b>clear it</b> to start over.</p>
                </div>            
        """;

        // Create drop-downs to filter search
        // Form Parameters
        String flags_drop = context.formParam("flags_drop");
        String metric_drop = context.formParam("metrics_drop");
        String start_date = context.formParam("start-date");
        String end_date = context.formParam("end-date");
        String orderby_drop = context.formParam("orderby_drop");
        String ascdesc_drop = context.formParam("ascdesc_drop");
        // Get Quality Flags for drop-down
        ArrayList<FLAG> flags = jdbc.getFlags();
        // Create drop-down element
        html = html + """
            <div>
                <form action ='/page2C.html' method='post'>
                    <div class = 'search_filters'>
                        <select id='flags_drop' name='flags_drop' class='drop-down'>
                        <option>--Quality Flag--</option>
        """;
        for (int i = 0; i < flags.size(); ++i) {
            String flag = flags.get(i).getFlag();
            if (flag.equals(flags_drop)){
                html = html + "<option selected>" + flag + "</option>";
            }
            else {
                html = html + "<option>" + flag + "</option>";
            }
        }

        html = html + "</select>";

        // Climate metric drop-down
        // Get Metrics for drop-down
        ArrayList<String> climateMetrics = jdbc.getMetrics();
        // Create drop-down element
        html = html+ """
                        <select id='metrics_drop' name='metrics_drop' class='drop-down'>
                        <option>--Climate Metric--</option>
        """;
        for (int i = 0; i < climateMetrics.size(); ++i) {
            String metric = climateMetrics.get(i);
            if (metric.equals(metric_drop)) {
                html = html + "<option selected>" + metric + "</option>";
            }
            else {
                html = html + "<option>" + metric + "</option>";
            }
        }

        html = html + "</select>";

        // Calendar feature
        html = html + "<div class='date-selector'>" +
                        "<label for='start-date'>Start Date:</label>" +
                        "<input type='date' id='start-date' name='start-date' min='1970-01-01' max='2020-12-31' value='" + start_date + "'>" +
                        "<label for='end-date'>End Date:</label>" +
                        "<input type='date' id='end-date' name='end-date' min='1970-01-01' max='2020-12-31' value='" + end_date + "'>" +
                    "</div>";

        // Sort feature
        html += "<select id='orderby_drop' name='orderby_drop' class='drop-down'>" +
                    "<option>--Sort Table--</option>" +
                    "<option>Station ID</option>" +
                    "<option>Date</option>" +
                    "<option>Climate Metric</option>" +
                "</select>";
        
        html += "<select id='ascdesc_drop' name='ascdesc_drop' class='drop-down'>" +
                    "<option>--Sort Direction--</option>" +
                    "<option>Ascending</option>" +
                    "<option>Descending</option>" +
                "</select>";
        // End form
        html = html + """
                    <div class='search-btns'>
                        <button type='reset'>Reset Filters</button>
                        <button type='submit'>Search</button>
                    </div>
                </div>
            </form>
        </div>
        <div class='table'>
        """;
        boolean tableDisplayed = false;

        // Handle table output if valid filters provided
        if (flags_drop != null && metric_drop != null && start_date != null && end_date != null &&
            !flags_drop.equals("--Quality Flag--") && !metric_drop.equals("--Climate Metric--") &&
            !orderby_drop.equals("--Sort Table--") && !ascdesc_drop.equals("--Sort Direction--")) {

            tableDisplayed = true;
            ArrayList<SearchResult> searchResults = jdbc.getSearch2CResults(flags_drop, metric_drop, start_date, end_date, orderby_drop, ascdesc_drop);

            // Render search results table
            html += "<div class='table'><div class='table-container'><table><thead><tr><th>Station Id</th><th>Date</th><th>"
                  + metric_drop;
                  if (metric_drop.equals("Precipitation") || metric_drop.equals("Evaporation")) {
                    html += " (mm)";
                  }
                  else if (metric_drop.equals("MaxTemp") || metric_drop.equals("MinTemp")) {
                    html += " (Celsius)";
                  }
                  else if (metric_drop.contains("Humid")) {
                    html += " (%)";
                  }
                  else if (metric_drop.equals("Sunshine")) {
                    html += " (Hours)";
                  }
                  else if (metric_drop.contains("Okta")) {
                    html += " (oktas)";
                  }
            html += "</th><th>" + metric_drop + " Quality</th></tr></thead><tbody>";

            for (SearchResult result : searchResults) {
                html += "<tr><td>" + result.getStationId() + "</td><td>" + result.getDate() + "</td><td>"
                      + result.getMetric() + "</td><td>" + result.getQuality() + "</td></tr>";
            }

            html += "</tbody></table></div></div>";
        } else {
            // Prompt user to select all filters if any are missing
            html += "<h3><i>Please select an option for each search filter</i></h3>";
        }

        // Save to summary form if table is visible
        if (tableDisplayed) {
            html += "<div class='search-table-btns'>";
            html += "<form action='/save-summary' method='post'>";
            html += "<input type='hidden' name='flags_drop' value='" + flags_drop + "'>";
            html += "<input type='hidden' name='metric_drop' value='" + metric_drop + "'>";
            html += "<input type='hidden' name='start-date' value='" + start_date + "'>";
            html += "<input type='hidden' name='end-date' value='" + end_date + "'>";
            html += "<input type='hidden' name='order' value='" + orderby_drop + "'>";
            html += "<input type='hidden' name='ascdesc' value='" + ascdesc_drop + "'>";
            html += "<div><button type='submit' class='export-search-btn'>Save to Summary</button></div>";
            html += "</form>";
            html += "<form action='/export-search' method='get'>" +
                        "<input type='hidden' name='metric' value='" + metric_drop + "'>" + 
                        "<input type='hidden' name='flag' value='" + flags_drop + "'>" + 
                        "<input type='hidden' name='start-date' value='" + start_date + "'>" + 
                        "<input type='hidden' name='end-date' value='" + end_date + "'>" + 
                        "<input type='hidden' name='order' value='" + orderby_drop + "'>" +
                        "<input type='hidden' name='ascdesc' value='" + ascdesc_drop + "'>" +
                        "<button type='submit' class='export-search-btn'>Export Search Table</button>" + 
                    "</form>" +
                    "</div>";
        }

        // Display summary table if data exists
        ArrayList<SummaryRow> summaryRows = jdbc.getSummaryRows();
        if (!summaryRows.isEmpty()) {
            html += "<div class='table'><h2>Summary Table</h2><div class='table-container'><table><thead><tr><th>Flag</th>";

            SummaryRow firstRow = summaryRows.get(0);
            for (String metric : firstRow.metricCounts.keySet()) {
                html += "<th>" + metric + "</th>";
            }
            html += "</tr></thead><tbody>";

            for (SummaryRow row : summaryRows) {
                html += "<tr><td>" + row.flag + "</td>";
                for (String metric : firstRow.metricCounts.keySet()) {
                    String val = row.metricCounts.get(metric);
                    html += "<td>" + (val != null ? val : "null") + "</td>";
                }
                html += "</tr>";
            }

            html += "</tbody></table></div></div>";
            html += """
                <div class='clear-summary-buttons'>
                    <form action='/clear-summary' method='post'>
                        <div>
                            <button type='submit' class='clear-btn'>Clear Summary Table</button>
                        </div>
                    </form>
                    <form action="/export-summary" method="get">
                        <div>
                            <button type="submit" class='export-summary-btn'>Export Summary Table</button>
                        </div>
                    </form>
                </div>
                    """;
        }

        // Close HTML content and body
        html += "</div></body></html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }
}