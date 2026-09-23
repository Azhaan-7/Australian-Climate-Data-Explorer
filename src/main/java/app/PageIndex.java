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
 * @editor David Eccles, 2025. email: david.eccles@rmit.edu.au
 */
public class PageIndex implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/";
    

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<String> nameAndTemp = jdbc.getNameAndMinTemp();
         ArrayList<String> nameAndRain = jdbc.getNameAndMaxRain();
        
        String html = "<html>";

        // Add some Header information
        html = html + "<head>" + 
               "<title>Homepage</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='mission.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='common.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='navbar.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='buttons.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='search_filters.css' />";
        html += "    <link rel='stylesheet' type='text/css' href='ST2A.css' />";
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
        
        
//add the title of the page
        html = html + """
            <div class = 'center_heading'>
            <b>Decades of Data, One Clear Forecast</b>
            </div>
            """;

//the line to capture user attention (think its important acc. to Neilson's Hueristics)       
        html = html + """
         <div class='center_text'>
            <b>Explore weather patterns across time - from the
            coldest nights to the heaviest storms</b><br>
            <br>
         
            Includes rainfall, temperature, sunshine, humidity and more!
            </div>
            """;
        html = html +"""
        
                <div class='body_box_3'>
                <div class = 'center_heading_B'>
                <b>Data Range: 1970 - 2020</b>
                </div>
                </div>
                """;    
            
        //  <div class='body_box'>
        //  <img src='snowflake.png' class='side-image' alt='BOM logo' height='35' width = '35'>
       
        //     <b>  Minimum Temperature   </b>
        //     <ul>
        //     <div class = 'left_align'>
        //     <div class = 'ul_li'>
        //     """;
        //     // html+="<li> <b>Name: "+nameAndTemp.get(0)+"</li>";
        //     // html+="<li> Temperature: "+nameAndTemp.get(1)+"°C"+"</li>";
        //     // html+="<li> Date: "+nameAndTemp.get(2)+"</li>";
        //     // html+="</b></div>";
        //     html+="<li> <b>Name: "+"placeholder1"+"</li>";
        //     html+="<li> Rainfall: "+"placeholder2"+" mm</li>";
        //     html+="<li> Date: "+"placeholder3"+"</li>";
        //     html+="</b></div>";
        //     html = html + """
         
        //     </div>
        //     </ul>
        //     </div>
        //     """;
        //     // For rainfall
        //     html = html + """
        //  <div class='body_box_2'>
        //  <img src='rain.png' class='side-image' alt='BOM logo' height='50' width = '50'>
       
        //     <b>  Maximum Rain   </b>
        //     <ul>
        //     <div class = 'left_align'>
        //     <div class = 'ul_li'>
        //     """;
        //     // html+="<li> <b>Name: "+nameAndRain.get(0)+"</li>";
        //     // html+="<li> Rainfall: "+nameAndRain.get(1)+" mm</li>";
        //     // html+="<li> Date: "+nameAndRain.get(2)+"</li>";
        //     // html+="</b></div>";
        //     html+="<li> <b>Name: "+"placeholder1"+"</li>";
        //     html+="<li> Rainfall: "+"placeholder2"+" mm</li>";
        //     html+="<li> Date: "+"placeholder3"+"</li>";
        //     html+="</b></div>";
        //     html = html + """
                    
        //     </div>
        //     </ul>
        //     </div>
            
        //below box container to encompasss both boxes for rainfall and temperature, they didnt align properly no anybody elses' end.
        //span was used to solve the title not being in line, which would in turn cause the icons to the side to be super imposed on the escape hatch.
      html = html + """
<div class="box-container">

  
   <div class="body_box">
      <div class="box-header">
         <img src='snowflake.png' class='side-icon' alt='Snowflake'>
         <span><b>Minimum Temperature Observed</b></span>
      </div>
      <ul>
         <div class="left_align">
            <div class="ul_li">
            <b>
            """;
             html+="<li> Name: "+nameAndTemp.get(0)+"</li>";
             html+="<li> Temperature: "+nameAndTemp.get(1)+" Celsius</li>";
             html+="<li> Date: "+nameAndTemp.get(2)+"</li>";
               
               // html+="<li>Name: placeholder1</li>";
               // html+="<li>Temperature: placeholder2 Celsius</li>";
               // html+="<li>Date: placeholder3</li>";
html+="""
      
      
               </b>
            </div>
         </div>
      </ul>
   </div>
""";
   html+="""
         
         
   <div class="body_box">
      <div class="box-header">
         <img src='rain.png' class='side-icon' alt='Rain'>
         <span><b>Maximum Rainfall Observed</b></span>
      </div>
      <ul>
         <div class="left_align">
            <div class="ul_li">
            <b>
            """;
              html+="<li> Name: "+nameAndRain.get(0)+"</li>";
            html+="<li> Rainfall: "+nameAndRain.get(1)+" mm</li>";
            html+="<li> Date: "+nameAndRain.get(2)+"</li>";    
                  
               // html+="<li>Name: placeholder1</li>";
               // html+="<li>Rainfall: placeholder2 Millimeters</li>";
               // html+="<li>Date: placeholder3</li>";
               html+="""
                     
                     
               </b>
            </div>
         </div>
      </ul>
   </div>

</div>
""";


        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
