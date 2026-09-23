package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST3A implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3A.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        JDBCConnection jdbc = new JDBCConnection();
        String html = "<html>";

        // Add some Head information
       html += "<head>";
        html = html + "<title>View Station Similarities</title>";
        html = html + "<link rel='stylesheet' type='text/css' href='mission.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='common.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='navbar.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='buttons.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='search_filters.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='ST2A.css' />";
        html += "</head>";

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
            <div class='center_heading_B'>
                <b>View Station Similarities</b>
            </div>
        """;

        html = html + """
                <div class='page-description'>
                    <p><b>Welcome to the View Weather Station Similarity Tool!</b></p>
                    <p>Use this page to search climate data similarities by selecting a <b>Reference Climate Station</b>, <b>Number of Stations</b> you'd like to compare to, <b>two time periods</b> and a <b>Climate Metric</b> using the filters below. You can then:</p>
                        <ul>
                            <li>Click <b>Search</b> to view matching records.</li>
                            <li>Click <b>Reset Filters</b> to clear your selections.</li>
                            <li><b>Export</b> your search results as a .csv file.</li>

                        </ul>
                    
                </div>            
        """;
        String station_drop = context.formParam("station_drop");
        String metrics_drop = context.formParam("metrics_drop");
        String start_date = context.formParam("start-date");
        String end_date = context.formParam("end-date");
        String start_date_1 = context.formParam("start-date-1");
        String end_date_1 = context.formParam("end-date-1");
        String numberOfStations = context.formParam("no_of_stations");
        
        html = html + """
        <form action='/page3A.html' method='post'>
        <div class='filter_row_single'>
""";
        // Get State codes and metrics for teh drop downs
        ArrayList<String> stations = jdbc.getWeatherStations();
        ArrayList<String> metrics = jdbc.getMetrics();
        String stationID = jdbc.getStationID(station_drop);
        // Stat Dropdown
        html = html + """     
          <div class='filter_box'>
              <select id='station_drop' name='station_drop' class='drop-down' style= "width: 280px">
                   <option>--Reference Weather Station--</option>
                   """;
        for (String code : stations) {
            String display = code; //i dont think it there are any dots in between station names    
            if (code.equals(station_drop)) {
                html = html+ "<option value='" + code + "' selected>" + display + "</option>";
            } else {
                html = html + "<option value='" + code + "'>" + display + "</option>";
            }
        }
        html = html + """
               </select>
      </div>
            </div>
            """;
            // Latitude minimum text field
        html = html + "<div class = 'filter_row '>";
        html = html+ "    <div class='filter_box'>";
        html = html + "        <input type='number' name='no_of_stations' class='drop-down' placeholder='Number of Stations' value='" + (numberOfStations != null ? numberOfStations : "") + "'>";

       
        html = html+ "    </div>";

        // Date selector (thanks to Kieran)
        html = html + "<div class='date-selector'>" +
                        "<label for='start-date'>Start Date:</label>" +
                        "<input type='date' id='start-date' name='start-date' min='1970-01-01' max='2020-12-31' value='" + start_date + "'>" +
                        "<label for='end-date'>End Date:</label>" +
                        "<input type='date' id='end-date' name='end-date' min='1970-01-01' max='2020-12-31' value='" + end_date + "'>" +
                    "</div>";
        html = html + "<div class='date-selector'>" +
                        "<label for='start-date'>Start Date:</label>" +
                        "<input type='date' id='start-date-1' name='start-date-1' min='1970-01-01' max='2020-12-31' value='" + start_date_1 + "'>" +
                        "<label for='end-date'>End Date:</label>" +
                        "<input type='date' id='end-date-1' name='end-date-1' min='1970-01-01' max='2020-12-31' value='" + end_date_1 + "'>" +
                    "</div>";            

        

        // Metric drop-down menu
        html = html + """
       
        <div class='filter_box'>
        <select id='metrics_drop' name='metrics_drop' class='drop-down'>
        <option>--Metric--</option>
        """;
        for (String metric : metrics) {
            if (metric.equals(metrics_drop)) {
                html += "<option value='" + metric + "' selected>" + metric + "</option>";
            } else {
                html += "<option value='" + metric + "'>" + metric + "</option>";
            }
        }
        html = html + """

              </select>
                </div>

                <div class='search-btns' style='margin-left: 70%;'>
                    <button type='reset'>Reset Filters</button>
                    <button type='submit'>Search</button>
                </div>

                </div>
                </form>
                """;
                

html = html + "<div class = 'table'>";
if (station_drop != null && metrics_drop != null && start_date != null && end_date != null && start_date_1 != null && end_date_1 != null && !station_drop.equals("--Reference Weather Station--") && numberOfStations != null && !numberOfStations.trim().isEmpty() && !metrics_drop.equals("--Metric--")){
    int numStations = Integer.parseInt(numberOfStations.trim());
;

//figuring out why no table
System.out.println("station_drop = " + station_drop);
System.out.println("metrics_drop = " + metrics_drop);
System.out.println("start_date = " + start_date);
System.out.println("end_date = " + end_date);
System.out.println("start_date_1 = " + start_date_1);
System.out.println("end_date_1 = " + end_date_1);
System.out.println("numberOfStations = " + numberOfStations);
System.out.println("stationID = " + stationID);

ArrayList<SearchResult> stationResults = jdbc.getSimilarStations( stationID, metrics_drop, start_date, end_date, start_date_1, end_date_1, numStations+1); //numStations plus 1, user expects 5 stations compared to, not 

if (!stationResults.isEmpty()) {
    html += "<div class='table'><div class='table-container'><table class='styled-table'>";
    String unit = "";
if (metrics_drop.equals("Precipitation") || metrics_drop.equals("Evaporation")) {
    unit = " (mm)";
} else if (metrics_drop.equals("MaxTemp") || metrics_drop.equals("MinTemp")) {
    unit = " (Celsius)";
} else if (metrics_drop.contains("Humid")) {
    unit = " (%)";
} else if (metrics_drop.equals("Sunshine")) {
    unit = " (Hours)";
} else if (metrics_drop.contains("Okta")) {
    unit = " (oktas)";
}
html += "<thead><tr>" +
        "<th>Station Name</th>" +
        "<th>Station ID</th>" +
        "<th>Avg Period 1 "+unit+"</th>" +
        "<th>Avg Period 2 "+unit+"</th>" +
        "<th>% Change</th>" +
        "<th>% Difference from "+station_drop+"</th>" +
        "</tr></thead><tbody>";

for (SearchResult s : stationResults) {
    html += "<tr>";
    html += "<td>" + s.getName() + "</td>";
    html += "<td>" + s.getStationId() + "</td>";
    html += "<td>" + s.getAvgPeriod1() + "</td>";
    html += "<td>" + s.getAvgPeriod2() + "</td>";
    html += "<td>" + s.getPercentChange() + "</td>";
    html += "<td>" + s.getDiffFromRef() + "</td>";
    html += "</tr>";
}
    html += "</tbody></table></div>";
    html += "<form action='/export-st3a' method='get'>";
    html += "<input type='hidden' name='station_id' value='" + stationID + "'>";
    html += "<input type='hidden' name='metric' value='" + metrics_drop + "'>";
    html += "<input type='hidden' name='start_date' value='" + start_date + "'>";
    html += "<input type='hidden' name='end_date' value='" + end_date + "'>";
    html += "<input type='hidden' name='start_date_1' value='" + start_date_1 + "'>";
    html += "<input type='hidden' name='end_date_1' value='" + end_date_1 + "'>";
    html += "<input type='hidden' name='num_stations' value='" + numberOfStations + "'>";
    html += "<div class='search-btns' style='margin-left: 400%; margin-top: 10%'><button type='submit' class='export-search-btn'>Export Table</button></div>";

    html += "</form>";

} else {
    html += "<h3><i>No similar stations found for the selected metric and time periods.</i></h3>";
}
}


        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
