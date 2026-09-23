package app;

import java.util.ArrayList;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class PageST2A implements Handler {

    public static final String URL = "/page2A.html";
    @Override
    public void handle(Context context) throws Exception {

        

        JDBCConnection jdbc = new JDBCConnection();
        

        String html = "";

        html += "<!DOCTYPE html>";
        html += "<html>";

        html += "<head>";
        html = html + "<title>Focused View by Climate Station</title>";
        html = html + "<link rel='stylesheet' type='text/css' href='mission.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='common.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='navbar.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='buttons.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='search_filters.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='ST2A.css' />";
        html += "</head>";

        html += "<body>";
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
        html = html + """
            <div class='center_heading_B'>
                <b>View by Weather Station</b>
            </div>
        """;
         html = html + """
                <div class='page-description'>
                    <p><b>Welcome to the Focused View by Weather Station Tool!</b></p>
                    <p>Use this page to search climate data by selecting a <b>State</b>, <b>Climate Metric</b>, and a <b>latitude range</b> using the filters below. You can also <b>sort</> on the basis of different columns</b>. You can then:</p>
                        <ul>
                            <li>Click <b>Search</b> to view matching records.</li>
                            <li>Click <b>Reset Filters</b> to clear your selections.</li>
                            <li><b>Export</b> your search results as a .csv file.</li>
                            <li><b>Save</b> a search to the <b>Summary Table</b> to track how many weather stations match each region and average metric.</li>
                        </ul>
                    <p>As you save more results, the Summary Table will grow to reflect all searches made during your session. You can <b>export the Summary Table</b> or <b>clear it</b> to start over.</p>
                </div>            
        """;
        String states_drop = context.formParam("states_drop");
        String metrics_drop = context.formParam("metrics_drop");
        String startLat = context.formParam("start_lat");
        String endLat = context.formParam("end_lat");
        String orderby_drop = context.formParam("orderby_drop");
        String ascdesc_drop = context.formParam("ascdesc_drop");
        html = html + """
        <form action='/page2A.html' method='post'>
        <div class='filter_row'>
""";
        // Get State codes and metrics for teh drop downs
        ArrayList<String> stateCodes = jdbc.getStates();
        ArrayList<String> metrics = jdbc.getMetrics();
        // State Dropdown
        html = html + """     
          <div class='filter_box'>
              <select id='states_drop' name='states_drop' class='drop-down'>
                   <option>--State--</option>
                   """;
        for (String code : stateCodes) {
            String display = code.replace(".", ""); //issues with NSW and other states with a . in the middle of the short form
            if (code.equals(states_drop)) {
                html = html+ "<option value='" + code + "' selected>" + display + "</option>";
            } else {
                html = html + "<option value='" + code + "'>" + display + "</option>";
            }
        }
        html = html + """
               </select>
      
            </div>
            """;

        // Latitude minimum text field
        html = html+ "    <div class='filter_box'>";
        html = html + "        <input type='text' name='end_lat' class='drop-down' placeholder='Start Latitude' value='" + (endLat != null ? endLat : "") + "'>";
       
        html = html+ "    </div>";

        // Latitude maximum text field, for some reason the triple quote notation doesn't work with the input for latitude
        html = html + "    <div class='filter_box'>";
        html = html + "        <input type='text' name='start_lat' class='drop-down' placeholder='End Latitude' value='" + (startLat != null ? startLat : "") + "'>";
        
        html = html + "    </div>";

        

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
                """;
                html += "<select id='orderby_drop' name='orderby_drop' class='drop-down'>" +
                    "<option>--Sort Table--</option>" +
                    "<option>Station ID</option>"+
                    "<option>Station Name</option>" +
                    "<option>Latitude</option>" +
                    "<option>Climate Metric</option>" +
                    "<option>Region</option>"+

                "</select>";
        
        html += "<select id='ascdesc_drop' name='ascdesc_drop' class='drop-down'>" +
                    "<option>--Sort Direction--</option>" +
                    "<option>Ascending</option>" +
                    "<option>Descending</option>" +
                "</select>";
                        
                        
html+="""
        
        
                <div class='search-btns'>
                    <button type='reset'>Reset Filters</button>
                    <button type='submit'>Search</button>
                </div>

                </div>
                </form>
                """;

        html = html + "<div class = 'table'>";
        boolean tableDisplayed = false;
        if (states_drop != null && metrics_drop != null && startLat != null && endLat != null && orderby_drop != null && ascdesc_drop != null &&
            !states_drop.equals("--State--") && !metrics_drop.equals("--Metric--") && !startLat.equals("Start Latitude")&& !endLat.equals("End Latitude")&&!orderby_drop.equals("--Sort Table--") && !ascdesc_drop.equals("--Sort Direction--")){
                tableDisplayed = true;
                ArrayList<SearchResult> searchResults = jdbc.viewbystation(states_drop, startLat, endLat, metrics_drop,orderby_drop,ascdesc_drop);
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

                html += "<div class='table'><div class='table-container'><table><thead><tr>" +
                "<th>Station ID</th>" +
                "<th>Station Name</th>" +
                "<th>" + metrics_drop + unit+ "</th>" +
                "<th>Latitude</th>" +
                "<th>Region</th>" +
                "</tr></thead><tbody>";

                for (SearchResult result : searchResults) {
                html += "<tr><td>" + result.getStationId() + "</td><td>" + result.getName() + "</td><td>"
                      + result.getMetric() + "</td><td>" + result.getLat() + "</td><td>"+result.getRegion()+"</td></tr>";
            }
            html += "</tbody></table></div></div>";
        } else {
            // Prompt user to select all filters if any are missing
            html += "<h3><i>Please select an option for each search filter</i></h3>";
        }
        if (tableDisplayed) {
    html += "<div class='search-table-btns'>";

    // Save to Summary Button
    html += "<form action='/save-region-summary' method='post'>";
    html += "<input type='hidden' name='states_drop' value='" + states_drop + "'>";
    html += "<input type='hidden' name='metrics_drop' value='" + metrics_drop + "'>";
    html += "<input type='hidden' name='start_lat' value='" + startLat + "'>";
    html += "<input type='hidden' name='end_lat' value='" + endLat + "'>";
    html += "<div><button type='submit' class='export-search-btn'>Save to Summary</button></div>";
    html += "</form>";

    // Export Search Table Button
    html += "<form action='/export-station-search' method='get'>";
    html += "<input type='hidden' name='states_drop' value='" + states_drop + "'>";
    html += "<input type='hidden' name='metrics_drop' value='" + metrics_drop + "'>";
    html += "<input type='hidden' name='start_lat' value='" + startLat + "'>";
    html += "<input type='hidden' name='end_lat' value='" + endLat + "'>";
    html += "<div><button type='submit' class='export-search-btn'>Export Search Table</button></div>";
    html += "</form>";

    html += "</div>";
}


// Always fetch the summary table
ArrayList<RegionSummaryRow> regionRows = jdbc.getAllRegionSummaryRows();

if (!regionRows.isEmpty()) {
    

    html += "<div class='table'><div class='table-container'><h3>Region Summary</h3><table><thead><tr>";
    html += "<th>Region</th>";

    RegionSummaryRow first = regionRows.get(0);
    for (String col : first.metricStats.keySet()) {
        html += "<th>" + col + "</th>";
    }
    html += "</tr></thead><tbody>";

    for (RegionSummaryRow row : regionRows) {
        html += "<tr><td>" + row.region + "</td>";
        for (String col : row.metricStats.keySet()) {
    String rawValue = row.metricStats.get(col);
    String formattedValue = rawValue;

    try {
        if (col.equalsIgnoreCase("StationCount")) {
            int intVal = (int) Double.parseDouble(rawValue);
            formattedValue = String.valueOf(intVal);
        } else {
            double val = Double.parseDouble(rawValue);
            formattedValue = String.format("%.4f", val);
        }
    } catch (NumberFormatException ignored) {
        // Leave non-numeric values as-is
    }

    html += "<td>" + formattedValue + "</td>";
}


        html += "</tr>";
    }

    html += "</tbody></table></div></div>";

    
    html += """
    <div class='clear-summary-buttons'>
        <form action='/clear-region-summary' method='post'>
            <div>
                <button type='submit' class='clear-btn'>Clear Summary Table</button>
            </div>
        </form>
        <form action='/export-region-summary' method='get'>
            <div>
                <button type='submit' class='export-summary-btn'>Export Summary Table</button>
            </div>
        </form>
    </div>
""";

}



            








        
        html += "</body>";
        html += "</html>";

        context.html(html);
    }

    public static String getURL() {
        return URL;
    }
}
