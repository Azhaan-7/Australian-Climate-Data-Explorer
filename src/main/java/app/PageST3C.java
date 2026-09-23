package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST3C implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3C.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";
        JDBCConnection jdbc = new JDBCConnection();

        // Add some Head information
        html = html + "<head>" + 
               "<title>Cross Metric Impact</title>";

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
                <h1>Cross Metric Impact</h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
                <div class='page-description'>
                    <p><b>Welcome to the Cross Metric Impact Search Tool</b></p>
                    <p>This webpage allows you to view the correlation between different <b>Climate Metrics</b></p>
                    <p>To search climate data first select a <b>Time Period</b>, a reference <b>Weather Station</b>, and a reference <b>Climate Metric</b> using the filters below. You can then:</p>
                        <ul>
                            <li>Click <b>Search</b> to view matching records.</li>
                            <li>Click <b>Reset Filters</b> to clear your selections.</li>
                            <li><b>Export</b> your search results as a .csv file.</li>
                        </ul>
                    <p>This tool will populate a table with <b>correlated data</b>, that being data that shows a trend of either increasing or decreasing alongside the selected reference <b>Climate Metric</b></p>
                </div> 
        """;

        // Begin creating drop downs
        // Establish form Params
        String station_drop = context.formParam("station_drop");
        String metric_drop = context.formParam("metric_drop");
        String start_date = context.formParam("start_date");
        String end_date = context.formParam("end_date");

        // Get station names for drop down
        ArrayList<String> stations = jdbc.getStationNames();
        // Construct Station Drop Down
        html += """
                <div>
                    <form action ='/page3C.html' method='post'>
                        <div class='search_filters'>
                            <select id='station_drop' name='station_drop' class='drop-down'>
                            <option>--Weather Station--</option>
                """;
        for (int i = 0; i < stations.size(); ++i) {
            String station = stations.get(i);
            if (station.equals(station_drop)) {
                html += "<option selected>" + station + "</option>";
            }
            else {
                html += "<option>" + station + "</option>";
            }
        }

        html += "</select>";

        // Construct metric drop down
        // Get metrics
        ArrayList<String> climateMetrics = jdbc.getMetrics();
        // Create Drop down
        html += """
                <select id='metric_drop' name='metric_drop' class='drop-down'>
                <option>--Climate Metric--</option>
        """;
        for (int i = 0; i < climateMetrics.size(); ++i) {
            String metric = climateMetrics.get(i);
            if (metric.equals(metric_drop)) {
                html += "<option selected>" + metric + "</option>";
            }
            else {
                html += "<option>" + metric + "</option>";
            }
        }

        html += "</select>";

        // Time Period selector
        html += "<div class='date-selector'>" +
                    "<label for='start_date'>Start Date:</label>" +
                    "<input type='date' id='start_date' name='start_date' min='1970-01-01' max='2020-12-31' value='" + start_date + "'>" +
                    "<label for='end_date'>End Date:</label>" +
                    "<input type='date' id='end_date' name='end_date' min ='1970-01-01' max='2020-12-31' value='" + end_date + "'>" +
                "</div>";

        // End form
        html += """
                    <div class='search-btns'>
                        <button type='reset'>Reset Filters</button>
                        <button type='submit'>Search</button>
                    </div>
                </div>
            </form>
        </div>
        <div class='table'>
        """;

        // Create Table
        boolean tableDisplayed = false;

        // Handle table output if valid filters provided
        if (station_drop != null && metric_drop != null && start_date != null && end_date != null && 
        !station_drop.equals("--Weather Station--") && !metric_drop.equals("--Climate Metric--") ) {

            tableDisplayed = true;
            ArrayList<SearchResult> searchResults = jdbc.getSearch3CResults(station_drop, metric_drop, start_date, end_date);
            // Parse date input
            LocalDate start = LocalDate.parse(start_date);
            LocalDate end = LocalDate.parse(end_date);
            long daysBetween = ChronoUnit.DAYS.between(start, end);
            LocalDate midPoint = start.plusDays(daysBetween / 2);
            // Render search results table
            html += "<div class='table'><div class='table-container'><table><thead><tr><th>Climate Metric</th><th>Total (" + start.getYear() + "-" + midPoint.getYear() + ")</th>" +
                    "<th>Total (" + midPoint.getYear() + "-" + end.getYear() +")</th><th>Change(%)</th><th>Metric Trend (correlation)</th></tr></thead><tbody>";
            
            int rowMark = 0;
            for (SearchResult result : searchResults) {
                html += "<tr><td>" + result.getMetric();
                if (result.getMetric().equals("Precipitation") || result.getMetric().equals("Evaporation")) {
                    html += " (mm)";
                  }
                  else if (result.getMetric().equals("MaxTemp") || result.getMetric().equals("MinTemp")) {
                    html += " (Celsius)";
                  }
                  else if (result.getMetric().contains("Humid")) {
                    html += " (%)";
                  }
                  else if (result.getMetric().equals("Sunshine")) {
                    html += " (Hours)";
                  }
                  else if (result.getMetric().contains("Okta")) {
                    html += " (oktas)";
                  } 
                html += "</td><td>" + result.getSumEarly() + "</td>" +
                        "<td>" + result.getSumLate() + "</td><td>" + result.getPercentChange() + "</td>";
                if (rowMark == 0) {
                    html += "<td>(Selected Metric)</td>";
                }
                else if (rowMark == 1) {
                    html += "<td>Positive Correlation</td>";
                }
                else if (rowMark == 2) {
                    html += "<td>Negative Correlation</td>";
                }
                else if (rowMark == 3) {
                    html += "<td>Neutral Correlation</td>";
                }
                ++rowMark;
                html += "</tr>";
            }

            html += "</tbody></table></div></div>";
        } else {
            // Prompt user to select all filters if any are missing
            html += "<h3><i>Please select an option for each search filter</i></h3>";
        }

        // Export table form
        if (tableDisplayed) {
            html += "<div class='search-table-btns'>" +
                        "<form action='/export-search-3C' method='get'>" +
                            "<input type='hidden' name='station' value='" + station_drop + "'>" +
                            "<input type='hidden' name='metric' value='" + metric_drop + "'>" +
                            "<input type='hidden' name='start-date' value='" + start_date + "'>" +
                            "<input type='hidden' name='end-date' value='" + end_date + "'>" +
                            "<button type='submit' class='export-search-btn'>Export Table</button>" +
                        "</form>" +
                    "</div>";
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
