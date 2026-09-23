package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

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
public class PageMission implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {

        
        // Create a simple HTML webpage in a String
        String html = """
        <html>
            <head>
                <title>Our Mission</title>
                    <link rel='stylesheet' type='text/css' href='common.css'/>
                    <link rel='stylesheet' type='text/css' href='navbar.css'/>
                    <link rel='stylesheet' type='text/css' href='data_summary.css'/>
                    <link rel='stylesheet' type='text/css' href='buttons.css'/>
                    <link rel='stylesheet' type='text/css' href='drop_down.css'/>
                    <link rel='stylesheet' type='text/css' href='search_filters.css'/>
                    <link rel='stylesheet' type='text/css' href='mission.css'/>
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
        
        html += """
                <div class='header'>
                    <h1>Our Mission</h1>
                </div>
        """;

        html += "<div class='content'>"; // start content

            html += "<div class='page-description'>";

            html += """
                <h3 class='header'>Our Goal</h3>
                <p>
                    We believe in the environment! Our site provides in depth, reliable and non bias information. We have designed the site to be useable by people of all background, allowing everyone to interact and gain access to valuable data. It is important that everyone has access to this data so they can get an understanding of what is happening with the worlds climate. Anyone who sees this information will find it clear that we MUST do something, before it's too late. Decades of data, one clear forecast!
                </p>
            """;

            html += "</div>";

            html += "<div class='page-description'>";

            html += """
                <h3 class='header'>How To Use</h3>
                <p>
                    We provide multiple tools to analyse climate data provided by the Bureau of Meteorology.
                    <br><br>
                    There are several elements that you will encounter in each of our tools.
                    <br><br>
                    Below are some examples. Feel free to interact with them and familiarise yourself with their functionality!
                </p>
                <div class='tutorial-search-filters'>
                    <div class='tutorial'>
                        <select class='tutorial-drop-down'>
                            <option>--Drop Down Menu--</option>
                            <option>List</option>
                            <option>Of</option>
                            <option>Options</option>
                        </select>
                        <span class='tutorial-text'>Select something from the list!</span>
                    </div>
                    <div class='tutorial'>
                        <input type='number' placeholder='Text Field...' class='tutorial-drop-down'></input>
                        <span class='tutorial-text'>Type something in me!</span>
                    </div>
                    <div class='tutorial'>
                        <div class='tutorial-date-selector'>
                            <label>Date:</label>
                            <input type='date' min='2000-01-01' max='2025-12-01'>
                        </div>
                        <span class='tutorial-text'>Select a Date!</span>
                    </div>
                </div>
            """;

            html += "</div>";

        html += "</div>"; // end content

        html += "<h3 class='header'>Personas</h3>";

        html += "<div class='mission-grid'>"; // start personas
        for (Persona persona : getPersonas()) {
            html += persona.toHtml();
        }
        html += "</div>"; // end personas

        html += "<h3 class='header'>Meet The Team</h3>";

        html += "<div class='mission-grid'>"; // start footer
                
            for (GroupMember member : getGroupMembers()) {
                html += member.toHtml();
            }

        html += "</div>"; // end footer

        html += """
            </body>
        </html>
        """;

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    public ArrayList<Persona> getPersonas() {
        ArrayList<Persona> personas = new ArrayList<>();

        Connection connection = null;

        try {
            String query;
            ResultSet results;

            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            query = "SELECT * FROM Persona";
            results = statement.executeQuery(query);
            
            while (results.next()) {
                Persona persona = new Persona();
                persona.setName(results.getString("Name"));
                persona.setAge(results.getInt("Age"));
                persona.setGender(results.getString("Gender"));
                persona.setEthnicity(results.getString("Ethnicity"));
                persona.setProfession(results.getString("Profession"));
                persona.setBackground(results.getString("Background"));
                persona.setNeeds(results.getString("Needs"));
                persona.setGoals(results.getString("Goals"));
                persona.setSkills(results.getString("Skills"));
                persona.setImage(results.getString("Image"));

                personas.add(persona);
            }
            
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return personas;
    }

    public ArrayList<GroupMember> getGroupMembers() {
        ArrayList<GroupMember> groupMembers = new ArrayList<>();

        Connection connection = null;

        try {
            String query;
            ResultSet results;

            // Connect to JDBC data base
            connection = DriverManager.getConnection(JDBCConnection.DATABASE);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            query = "SELECT * FROM GroupMember";
            results = statement.executeQuery(query);
            
            while (results.next()) {
                GroupMember groupMember = new GroupMember();
                groupMember.setName(results.getString("Name"));
                groupMember.setStudentNum(results.getString("StudentNum"));
                groupMember.setSubTask(results.getString("SubTask"));

                groupMembers.add(groupMember);
            }
            
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return groupMembers;
    }

    public class Persona {
        private String name;
        private int age;
        private String gender;
        private String ethnicity;
        private String profession;
        private String background;
        private ArrayList<String> needs;
        private ArrayList<String> goals;
        private ArrayList<String> skills; 
        private String image;

        public Persona() {
            this.name = "ERROR";
            this.age = -1;
            this.gender = "ERROR";
            this.ethnicity = "ERROR";
            this.profession = "ERROR";
            this.background = "ERROR";
            this.needs = new ArrayList<>();
            this.goals = new ArrayList<>();
            this.skills = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEthnicity() {
            return ethnicity;
        }

        public void setEthnicity(String ethnicity) {
            this.ethnicity = ethnicity;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public ArrayList<String> getNeeds() {
            return needs;
        }

        public void setNeeds(ArrayList<String> needs) {
            this.needs = needs;
        }

        public void setNeeds(String needs) {
            this.needs = new ArrayList<>(Arrays.asList(needs.split("\\|")));
        }

        public ArrayList<String> getGoals() {
            return goals;
        }

        public void setGoals(ArrayList<String> goals) {
            this.goals = goals;
        }

        public void setGoals(String goals) {
            this.goals = new ArrayList<>(Arrays.asList(goals.split("\\|")));
        }

        public ArrayList<String> getSkills() {
            return skills;
        }

        public void setSkills(ArrayList<String> skills) {
            this.skills = skills;
        }

        public void setSkills(String skills) {
            this.skills = new ArrayList<>(Arrays.asList(skills.split("\\|")));
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String toHtml() {
            String html = "";

            html += "<div class='mission-grid-element'>";

            html += "<img class='persona-face' src='" + getImage() + "'/>";
            
            html += "<ul>";

            html += "<li>Name: <a>" 
                    + this.getName() + "</a></li>";
            html += "<li>Age: <a>" 
                    + this.getAge() + "</a></li>";
            html += "<li>Gender: <a>" 
                    + this.getGender() + "</a></li>";
            html += "<li>Ethnicity: <a>" 
                    + this.getEthnicity() + "</a></li>";
            html += "<li>Profession: <a>" 
                    + this.getProfession() + "</a></li>";
            html += "<li>Background: <a>" 
                    + this.getBackground() + "</a></li>";

            html += "<li>Needs:<ul>";
            for (String need : this.getNeeds()) {
                html += "<li><a>" + need + "</a></li>";
            }
            html += "</ul></li>";

            html += "<li>Goals:<ul>";
            for (String goal : this.getGoals()) {
                html += "<li><a>" + goal + "</a></li>";
            }
            html += "</ul></li>";

            html += "<li>Skills:<ul>";
            for (String skill : this.getSkills()) {
                html += "<li><a>" + skill + "</a></li>";
            }
            html += "</ul></li>";

            html += "</ul>";
            html += "</div>";

            return html;
        }
    }

    public class GroupMember {
        private String name;
        private String studentNum;
        private String subTask;

        public GroupMember() {
            this.name = "ERROR";
            this.studentNum = "ERROR";
            this.subTask = "ERROR";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStudentNum() {
            return studentNum;
        }

        public void setStudentNum(String studentNum) {
            this.studentNum = studentNum;
        }

        public String getSubTask() {
            return subTask;
        }

        public void setSubTask(String subTask) {
            this.subTask = subTask;
        }

        public String toHtml() {
            String html = "";

            html += "<div class='mission-grid-element'>";

            html += "<ul>";

            html += "<li>Name: <a>" 
                        + getName() + "</a></li>";
            html += "<li>Student #: <a>" 
                        + getStudentNum() + "</a></li>";
            html += "<li>Sub Task: <a>" 
                        + getSubTask() + "</a></li>";

            html += "</ul>";

            html += "</div>";

            return html;
        }
    }
}
