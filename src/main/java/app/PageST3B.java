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
public class PageST3B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3B.html";

    @Override
    public void handle(Context context) throws Exception {
        JDBCConnection jdbc = new JDBCConnection();

        // Add some Head information
        String html = """
        <html>
            <head>
                <title>Metric Similarities</title>
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

        html = html + """
            <div class='header'>
                <h1>Metric Similarities</h1>
            </div>
        """;

        html = html + "<div class='content'>";

        html = html + """
                <div class='page-description'>
                    <p><b>Welcome to the View Metric Similarities Tool</b></p>
                    <p>Use this page to compare metric similarities by selecting a <b>Climate Metrics</b>, <b>Number of Comparisons</b>, and a <b>Date Range</b> using the filters below. You can then:</p>
                        <ul>
                            <li>Click <b>Search</b> to view matching records.</li>
                            <li>Click <b>Reset Filters</b> to clear your selections.</li>
                            <li><b>Export</b> your search results as a .csv file.</li>
                        </ul>
                </div>            
        """;

        String metricDrop = context.formParam("metrics_drop");
        String numMetrics = context.formParam("num_metrics");
        String startDate = context.formParam("start-date");
        String endDate = context.formParam("end-date");
        
        ArrayList<String> metrics = jdbc.getMetrics();
        
        // Create drop-down element
        // metrics
        html = html + """
            <div>
                <form action ='/page3B.html' method='post'>
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

        html += "<input type='number' placeholder='Num Metrics...' id='num_metrics' name='num_metrics' class='drop-down' value='" + 
               numMetrics + "'>";

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

        if (metricDrop != null && startDate != null && endDate != null && numMetrics != null && !metricDrop.equals("--Climate Metric--")) {
            ArrayList<SearchResult> searchResults = jdbc.get3bResults(metricDrop, startDate, endDate, Integer.parseInt(numMetrics));

            tableDisplayed = true;

            html += "<div class='table-container'><table><thead><tr><th>Metric</th>";
            html += "<th>" + searchResults.get(0).sumEarly + "-" + searchResults.get(0).date + "</th>";
            html += "<th>" + searchResults.get(0).date + "-" + searchResults.get(0).sumLate + "</th>";
            html += "<th>% Change</th><th>Difference From " + metricDrop + "</th>";
            html += "</tr></thead><tbody>";

            for (SearchResult result : searchResults) {
                html += "<tr><td>" + result.metric + "</td><td>" + result.avgPeriod1 + "</td><td>" + result.avgPeriod2 + "</td><td>"
                        + result.percentChange + "</td><td>" + result.diffFromRef + "</td></tr>";
            }

            html += "</tbody></table></div>";
        } else {
            html = html + "<h3><i>Please select an option for each search filter</i></h3>";
        }

        if (tableDisplayed) {
            html += "<div class='search-table-btns'>";
            html += "<form action='/export-search-3b' method='get'>" +
                        "<input type='hidden' name='metric' value='" + metricDrop + "'>" + 
                        "<input type='hidden' name='start-date' value='" + startDate + "'>" + 
                        "<input type='hidden' name='end-date' value='" + endDate + "'>" + 
                        "<input type='hidden' name='num_metrics' value='" + numMetrics + "'>" +
                        "<button type='submit' class='export-search-btn'>Export Table</button>" + 
                    "</form>" +
                    "</div>";
        }

        html = html + "</div></div>";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
