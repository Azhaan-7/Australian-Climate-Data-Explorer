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
public class PageDataSummary implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/datasummary.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";
        JDBCConnection jdbc = new JDBCConnection();

        // Add some Head information
        html = html + "<head>" + 
               "<title>Dataset Summary</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='mission.css'/>";
        html = html + "<link rel='stylesheet' type='text/css' href='data_summary.css'/>";
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
                <h1>Understanding Our Data</h1>
            </div>
        """;

        // Add Div for page Content
        html = html + "<div class='content'>";

        // Add HTML for the page content
        html = html + """
            <p class= 'page-description'>

                This page is dedicated to helping you learn more about our data. It contains handy tools to look up descriptions of each of the available datasets included in this website, as well as a tool to provide definitions for specific attributes. To use these tools, simply select an option from the drop-down menus below, and click on the corresponding button to generate the descriptions!

            </p>
        """;

        String datasets_drop = context.formParam("datasets_drop");
        String attributes_drop = context.formParam("attributes_drop");    
        // Get Datasets for dropdown
        ArrayList<String> Datasets = jdbc.getDatasets();    
        // Add dropdown element for Datasets
        html = html + """
        <div class = 'summary-element'>
            <form action ='/datasummary.html' method='post'>
                <div class = 'form-group'>
                    <label for='datasets_drop'>
                    Choose a Dataset from this drop-down menu and click the 'Get Description!' button to learn more:
                    </label>
                    <select id='datasets_drop' name='datasets_drop' class='drop-down'>
                        <option>--Select a Dataset--</option>
        """;
        for (int i = 0; i < Datasets.size(); ++i) {
            String dataset = Datasets.get(i);
            if (dataset.equals(datasets_drop)) {
                html = html + "<option selected>" + dataset + "</option>";
            }
            else {
                html = html + "<option>" + dataset + "</option>";
            }
            
        }
        
        html = html + """
                    </select>
                <button type='submit' class='summary-btn'>Get Description!</button>
            </div>
            </form>
            <div class='output'>
        """;

        // Get the Database descriptions
        // Output if no dataset is selected
        if (datasets_drop == null){
            html = html + "<h2><i>Please select a dataset to learn more!</i></h2>";
        }
        else if (datasets_drop.equals("--Select a Dataset--")) {
            html = html + "<h2><i>Please select a dataset to learn more!</i></h2>";
        }
        
        // Otherwise find description
        else {
            html = html + jdbc.outputDescriptions(datasets_drop);
        }

        // End <div>
        html = html + "</div></div>";


        // Get attributes for attribute dropdown
        ArrayList<String> Attributes = jdbc.getAttributes();
        
        // Add dropdown element for Datasets
        html = html + """
        <div class= 'summary-element'>
            <form action ='/datasummary.html' method='post'>
                <div class = 'form-group'>
                    <label for='attributes_drop'>
                    Choose an Attribute from this drop-down menu and click the 'Get Definition!' button to learn more:
                    </label>
                    <select id='attributes_drop' name='attributes_drop' class='drop-down'>
                        <option>--Select an Attribute--</option>
        """;
        for (int i = 0; i < Attributes.size(); ++i) {
            String attribute = Attributes.get(i);
            if (attribute.equals(attributes_drop)) {
                html = html + "<option selected>" + attribute + "</option>";
            }
            else {
                html = html + "<option>" + attribute + "</option>";
            }
            
        }

        html = html + """
                    </select>
                <button type='submit' class='summary-btn'>Get Definition!</button>
            </div>
            </form>
            <div class='output'>
        """;

        // Get the Attribute's definition
        // Output if no Attribute is selected
        if (attributes_drop == null) {
            html = html + "<h2><i>Please select an attribute to learn more!</i></h2>";
        }
        else if (attributes_drop.equals("--Select an Attribute--")) {
            html = html + "<h2><i>Please select an attribute to learn more!</i></h2>";
        }

        // Otherwise find definition
        else {
            html = html + jdbc.outputDefinition(attributes_drop);
        }
        // End <div>
        html = html + "</div></div>";

        
        
        // Close Content div
        html = html + "</div>";

        // // Footer
        // html = html + """
            // <div class='footer'>
                // <p>COSC2803 - Studio Project Starter Code (ACC-Apr2025)</p>
            // </div>
        // """;

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }
}
