package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.naming.spi.DirStateFactory.Result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Class for Managing the JDBC Connection to a SQLLite Database.
 * Allows SQL queries to be used with the SQLLite Databse in Java.
 *
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Halil Ali, 2024. email: halil.ali@rmit.edu.au
 * @editor David Eccles, 2025 email: david.eccles@rmit.edu
 */

public class JDBCConnection {

    // Name of database file (contained in database folder)
    // this fixes the local path so you dont need to move the database
    public static final String DATABASE = "jdbc:sqlite:database/climate.db";
    

    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCConnection() {
        System.out.println("Created JDBC Connection Object");
    }

    /**
     * Get all of the Flag Descriptions
     */
    public ArrayList<FLAG> getFlags() {
        // Create the ArrayList of FlagQuality objects to return
        // Create an array called flags
        ArrayList<FLAG> flags = new ArrayList<FLAG>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // put in a timeout incase the db is not running
            statement.setQueryTimeout(30);

            // The SQL Query to be executed 
            String query = "SELECT * FROM FlagQuality";
            
            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Lookup the columns we need
                String flagtype     = results.getString("flag");
                String description  = results.getString("description");

                // Create an FLAG Object
                FLAG flagsObj = new FLAG(flagtype, description);

                // Add the FLAG object to the flags array
                flags.add(flagsObj);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the countries
        return flags;
    }

    // TODO: Add your required methods here
    
    // Method to get all Dataset names from database
    public ArrayList<String> getDatasets() {
        // Create ArrayList datasets
        ArrayList<String> datasets = new ArrayList<String>();
        
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // put in a timeout incase the db is not running
            statement.setQueryTimeout(30);

            // The SQL Query to be executed 
            String query = "SELECT DATASET FROM DataDescription";

            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                //Column Lookup
                String dataset = results.getString("DATASET");

                //Store the result into ArrayList
                datasets.add(dataset);
            }

            // Close Statement
            statement.close();

        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Return the datasets
        return datasets;
    } 
    
    // Method to get descriptions of a specific dataset
    public String outputDescriptions(String dataset){
        String html = "";
        html = html + "<h2>" + dataset + " Description</h2>";
        
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT DESCRIPTION FROM DataDescription WHERE DATASET = '" + dataset +"'";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            String description = results.getString("DESCRIPTION");
        
            html = html + " <ul class='summary-lists'><li>" + description + "</li></ul>";
        
        
        // Close the statement
        statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Return the html
        return html;
    }

    // Method to get all NTRLNames of attributes from database
    public ArrayList<String> getAttributes() {
        
        // Create ArrayList Attributes
        ArrayList<String> Attributes = new ArrayList<>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // put in a timeout incase the db is not running
            statement.setQueryTimeout(30);

            // The SQL Query to be executed 
            String query = "SELECT NTRLName FROM Metadata";

            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                //Column Lookup
                String attribute = results.getString("NTRLName");

                // Add results to ArrayList
                Attributes.add(attribute);
            }

            // Close statement
            statement.close();
            } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Return ArrayList
        return Attributes;
    }

    // Method to find attribute definitions
    public String outputDefinition(String attribute) {
        String html = "";
        html = html + "<h2>" + attribute + " Definition</h2>";

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT NTRLDescription FROM Metadata WHERE NTRLName = '" + attribute + "'";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            String definition = results.getString("NTRLDescription");

            html = html + " <ul class='summary-lists'><li>" + definition + "</li></ul>";

            // Close the statement
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Return the html
        return html;
    }
    public ArrayList<String> getNameAndMinTemp() {
    ArrayList<String> NameTempDate = new ArrayList<>();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

       String query = 
        "SELECT " +
        "    CAST(t.MinTemp AS REAL) AS MinimumTemperature, " +
        "    l.name AS StationName, " +
        "    t.DMY AS Date " +
        "FROM ( " +
        "    SELECT MinTemp, Location, DMY " +
        "    FROM ( " +
        "        SELECT MinTemp, Location, DMY FROM AAT " +
        "        UNION ALL SELECT MinTemp, Location, DMY FROM AET " +
        "        UNION ALL SELECT MinTemp, Location, DMY FROM NSW " +
        "        UNION ALL SELECT MinTemp, Location, DMY FROM VIC " +
        "        UNION ALL SELECT MinTemp, Location, DMY FROM QLD " +
        "        UNION ALL SELECT MinTemp, Location, DMY FROM SA " +
        "        UNION ALL SELECT MinTemp, Location, DMY FROM TAS " +
        "        UNION ALL SELECT MinTemp, Location, DMY FROM WA " +
        "        UNION ALL SELECT MinTemp, Location, DMY FROM NT " +
        "    ) " +
        "    ORDER BY CAST(MinTemp AS REAL) ASC " +
        "    LIMIT 1 " +
        ") t " +
        "JOIN location l ON l.site = t.Location;";





        ResultSet results = statement.executeQuery(query);

        if (results.next()) {
            String name = results.getString("StationName");

            String[] words = name.toLowerCase().split(" ");
            String titleCaseName = "";

            for (int i = 0; i < words.length; i++) {
                if (words[i].length() > 0) {
                    titleCaseName += Character.toUpperCase(words[i].charAt(0)) + words[i].substring(1);
                    if (i < words.length - 1) {
                        titleCaseName += " ";
                    }
                }
            }

            name = titleCaseName;

            
            String temp = results.getString("MinimumTemperature");
            String date = results.getString("Date");

            NameTempDate.add(name);
            NameTempDate.add(temp);
            NameTempDate.add(date);
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Close Error: " + e.getMessage());
        }
    }

    return NameTempDate;
}
public ArrayList<String> getNameAndMaxRain() {
    ArrayList<String> NameRainDate = new ArrayList<>();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String query = String.format(
    "SELECT " +
    "    CAST(r.Precipitation AS REAL) AS MaximumRainfall, " +
    "    l.name AS StationName, " +
    "    r.DMY AS Date " +
    "FROM ( " +
    "    SELECT Precipitation, Location, DMY " +
    "    FROM ( " +
    "        SELECT Precipitation, Location, DMY FROM AAT " +
    "        UNION ALL SELECT Precipitation, Location, DMY FROM AET " +
    "        UNION ALL SELECT Precipitation, Location, DMY FROM NSW " +
    "        UNION ALL SELECT Precipitation, Location, DMY FROM VIC " +
    "        UNION ALL SELECT Precipitation, Location, DMY FROM QLD " +
    "        UNION ALL SELECT Precipitation, Location, DMY FROM SA " +
    "        UNION ALL SELECT Precipitation, Location, DMY FROM TAS " +
    "        UNION ALL SELECT Precipitation, Location, DMY FROM WA " +
    "        UNION ALL SELECT Precipitation, Location, DMY FROM NT " +
    "    ) " +
    "    ORDER BY CAST(Precipitation AS REAL) DESC " +
    "    LIMIT 1 " +
    ") r " +
    "JOIN location l ON l.site = r.Location;"
);




        ResultSet results = statement.executeQuery(query);

        if (results.next()) {
            String name = results.getString("StationName");

            // Convert to title case manually using string concatenation
            String[] words = name.toLowerCase().split(" ");
            String titleCaseName = "";
            for (int i = 0; i < words.length; i++) {
                if (words[i].length() > 0) {
                    titleCaseName += Character.toUpperCase(words[i].charAt(0)) + words[i].substring(1);
                    if (i < words.length - 1) {
                        titleCaseName += " ";
                    }
                }
            }

            String rainfall = results.getString("MaximumRainfall");
            String date = results.getString("Date");

            NameRainDate.add(titleCaseName);
            NameRainDate.add(rainfall);
            NameRainDate.add(date);
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Close Error: " + e.getMessage());
        }
    }

    return NameRainDate;
}
    //Method to get climate metrics from DB
    public ArrayList<String> getMetrics() {

        // Create ArrayList ClimateMetrics
        ArrayList<String> ClimateMetrics = new ArrayList<>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // put in a timeout incase the db is not running
            statement.setQueryTimeout(30);

            // The SQL Query to be executed 
            String query = """
                    SELECT Field FROM Metadata 
                    WHERE Field NOT LIKE '%Qual%'
                    AND Field NOT LIKE '%Days%'
                    AND Field NOT LIKE 'Location'
                    AND Field NOT LIKE 'DMY'
            """;
            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                //Column Lookup
                String metric = results.getString("Field");

                // Add results to ArrayList
                ClimateMetrics.add(metric);
            }
            // Close statement
            statement.close();
            } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
            } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Return ArrayList
        return ClimateMetrics;
    }

    // Method for PageST2C search results
    public ArrayList<SearchResult> getSearch2CResults(String flag, String metric, String dateStart, String dateEnd, String order, String ascdesc) {
        ArrayList<SearchResult> SearchResults = new ArrayList<SearchResult>();
        
        // Need a String for metric + Qual
        String metricQual = metric + "Qual";
        
        // Adjust order and ascdesc for SQL
        if (order.equals("Station ID")) {
            order = "location";
        }
        else if (order.equals("Date")) {
            order = "DMY";
        }
        else if (order.equals("Climate Metric")) {
            order = metric;
        }

        if (ascdesc.equals("Ascending")) {
            ascdesc = "ASC";
        }
        else if (ascdesc.equals("Descending")) {
            ascdesc = "DESC";
        }

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            // put in a timeout incase the db is not running
            statement.setQueryTimeout(30);

            // The SQL Query to be executed 
            String query =  "SELECT location, DMY, " + metric + ", " + metricQual + " FROM AAT " +
                            "WHERE " + metric + " IS NOT NULL AND TRIM(" + metric + ") != '' AND " + metric + "Qual = '" + flag + "' AND DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                            "UNION ALL " +
                            "SELECT location, DMY, " + metric + ", " + metricQual + " FROM AET " +
                            "WHERE " + metric + " IS NOT NULL AND TRIM(" + metric + ") != '' AND " + metric + "Qual = '" + flag + "' AND DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                            "UNION ALL " +
                            "SELECT location, DMY, " + metric + ", " + metricQual + " FROM NSW " +
                            "WHERE " + metric + " IS NOT NULL AND TRIM(" + metric + ") != '' AND " + metric + "Qual = '" + flag + "' AND DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                            "UNION ALL " +
                            "SELECT location, DMY, " + metric + ", " + metricQual + " FROM NT " +
                            "WHERE " + metric + " IS NOT NULL AND TRIM(" + metric + ") != '' AND " + metric + "Qual = '" + flag + "' AND DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                            "UNION ALL " +
                            "SELECT location, DMY, " + metric + ", " + metricQual + " FROM QLD " +
                            "WHERE " + metric + " IS NOT NULL AND TRIM(" + metric + ") != '' AND " + metric + "Qual = '" + flag + "' AND DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                            "UNION ALL " +
                            "SELECT location, DMY, " + metric + ", " + metricQual + " FROM SA " +
                            "WHERE " + metric + " IS NOT NULL AND TRIM(" + metric + ") != '' AND " + metric + "Qual = '" + flag + "' AND DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                            "UNION ALL " +
                            "SELECT location, DMY, " + metric + ", " + metricQual + " FROM TAS " +
                            "WHERE " + metric + " IS NOT NULL AND TRIM(" + metric + ") != '' AND " + metric + "Qual = '" + flag + "' AND DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                            "UNION ALL " +
                            "SELECT location, DMY, " + metric + ", " + metricQual + " FROM VIC " +
                            "WHERE " + metric + " IS NOT NULL AND TRIM(" + metric + ") != '' AND " + metric + "Qual = '" + flag + "' AND DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                            "UNION ALL " +
                            "SELECT location, DMY, " + metric + ", " + metricQual + " FROM WA " +
                            "WHERE " + metric + " IS NOT NULL AND TRIM(" + metric + ") != '' AND " + metric + "Qual = '" + flag + "' AND DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "' " +
                            "ORDER BY " + order +" " + ascdesc + ";";
            // Put the SQL results into a result set
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            while (results.next()) {
                // Create SearchResult object
                SearchResult searchResults = new SearchResult();

                // Add data to object
                searchResults.stationId = results.getString("Location");
                searchResults.date = results.getString("DMY");
                searchResults.metric = results.getString(metric);
                searchResults.quality = results.getString(metricQual);

                // Add search results to the ArrayList
                SearchResults.add(searchResults);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Return ArrayList
        return SearchResults;
    }
    public ArrayList<String> getStates() {
    ArrayList<String> NameRainDate = new ArrayList<>();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String query = "SELECT StateCode FROM States;";



        ResultSet results = statement.executeQuery(query);

        while (results.next()) {
            String name = results.getString("StateCode");

            
            NameRainDate.add(name);
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Close Error: " + e.getMessage());
        }
    }

    return NameRainDate;
}
public ArrayList<String> getRegions(String stateCode) {
    ArrayList<String> regions = new ArrayList<>();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);


String query = "SELECT DISTINCT " +
               "CASE " +
               "  WHEN TRIM(region) = 'N.A.' THEN name " +
               "  ELSE region " +
               "END AS display_region " +
               "FROM Location " +
               "WHERE TRIM(State) = '" + stateCode + "' " +
               "ORDER BY display_region;";


        ResultSet results = statement.executeQuery(query);

        while (results.next()) {
            String name = results.getString("display_region");

          

            regions.add(name);
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Close Error: " + e.getMessage());
        }
    }

    return regions;
}

// Method to create summary table for page 2C
public void saveToSummary(String flag, String metric, int count) {
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        // Sanitize column name by quoting it
        String columnName = "\"" + metric + "QualityCount\"";

        // Ensure the SummaryTable exists
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS SummaryTable (
                flag TEXT PRIMARY KEY
            );
        """;
        statement.executeUpdate(createTableSQL);

        // Add the column if it doesn't exist
        String checkColumnSQL = "PRAGMA table_info(SummaryTable);";
        ResultSet rs = statement.executeQuery(checkColumnSQL);
        boolean columnExists = false;

        while (rs.next()) {
            String existingCol = rs.getString("name");
            if (existingCol.equals(metric + "QualityCount")) {
                columnExists = true;
                break;
            }
        }

        if (!columnExists) {
            String addColumnSQL = "ALTER TABLE SummaryTable ADD COLUMN " + columnName + " INTEGER;";
            statement.executeUpdate(addColumnSQL);
        }

        // Insert or update the count
        String insertOrUpdateSQL = "INSERT INTO SummaryTable (flag, " + columnName + ") VALUES (?, ?) " +
                                   "ON CONFLICT(flag) DO UPDATE SET " + columnName + " = COALESCE(" + columnName + ", 0) + ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(insertOrUpdateSQL)) {
            pstmt.setString(1, flag);
            pstmt.setInt(2, count);
            pstmt.setInt(3, count);
            pstmt.executeUpdate();
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error in saveToSummary: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Connection close error in saveToSummary: " + e.getMessage());
        }
    }
}

// Method to get the rows from Summary table
public ArrayList<SummaryRow> getSummaryRows() {
    ArrayList<SummaryRow> rows = new ArrayList<>();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String query = "SELECT * FROM SummaryTable";
        ResultSet results = statement.executeQuery(query);

        while (results.next()) {
            String flag = results.getString("flag");
            SummaryRow row = new SummaryRow(flag);

            for (int i = 1; i <= results.getMetaData().getColumnCount(); ++i) {
                String colName = results.getMetaData().getColumnName(i);
                if (!colName.equalsIgnoreCase("flag")) {
                    row.metricCounts.put(colName, results.getString(colName));
                }
            }

            rows.add(row);
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("Error reading SummaryTable: " + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    return rows;
}

// Method to drop the SummaryTable
public void clearSummaryTable() {
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String dropTableSQL = "DROP TABLE IF EXISTS SummaryTable;";
        statement.executeUpdate(dropTableSQL);

        statement.close();
    } catch (SQLException e) {
        System.err.println("Error clearing SummaryTable:" + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Connection close error in clearSummaryTable: " + e.getMessage());
        }
    }
}

// Method to get SummaryTable as csv file
public String getSummaryAsCSV() {
    StringBuilder csv = new StringBuilder();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        ResultSet results = statement.executeQuery("SELECT * FROM SummaryTable");
        int columnCount = results.getMetaData().getColumnCount();

        // Header row
        for (int i = 1; i <= columnCount; i++) {
            csv.append(results.getMetaData().getColumnName(i));
            if (i < columnCount) csv.append(",");
        }
        csv.append("\n");

        // Data rows
        while (results.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String value = results.getString(i);
                if (value != null) value = value.replace("\"", "\"\"");
                csv.append("\"").append(value).append("\"");
                if (i < columnCount) csv.append(",");
            }
            csv.append("\n");
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("Error exporting CSV: " + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Close error: " + e.getMessage());
        }
    }

    return csv.toString();
}

public String getSummary2BAsCSV() {
    StringBuilder csv = new StringBuilder();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        ResultSet results = statement.executeQuery("SELECT * FROM MetricSummary");
        int columnCount = results.getMetaData().getColumnCount();

        // Header row
        for (int i = 1; i <= columnCount; i++) {
            csv.append(results.getMetaData().getColumnName(i));
            if (i < columnCount) csv.append(",");
        }
        csv.append("\n");

        // Data rows
        while (results.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String value = results.getString(i);
                if (value != null) value = value.replace("\"", "\"\"");
                csv.append("\"").append(value).append("\"");
                if (i < columnCount) csv.append(",");
            }
            csv.append("\n");
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("Error exporting CSV: " + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Close error: " + e.getMessage());
        }
    }

    return csv.toString();
}

public ArrayList<SearchResult> viewbystation(String stateCode, String minLat, String maxLat, String metric, String order, String ascdesc) {
    ArrayList<SearchResult> StationDetails = new ArrayList<>();
    
    Connection connection = null;
    if (order.equals("Station ID")) {
                order = "s1.location";
            }
            else if (order.equals("Station Name")) {
                order = "l1.Name";
            }
            else if (order.equals("Climate Metric")) {
                order = "s1."+metric;
            }
            else if (order.equals("Latitude")) {
                order = "l1.lat";
            }
            else if (order.equals("Region")) {
                order = "l1.region";
            }

            if (ascdesc.equals("Ascending")) {
                ascdesc = "ASC";
            }
            else if (ascdesc.equals("Descending")) {
                ascdesc = "DESC";
            }
    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String query = "SELECT DISTINCT s1.Location AS 'Station Number', " +
               "l1.Name AS 'Station Name', l1.region AS 'Region', l1.lat AS 'Latitude', s1." + metric + " AS 'Metric' " +
               "FROM " + stateCode + " s1 " +
               "JOIN Location l1 ON s1.Location = l1.site " +
               "WHERE CAST(l1.lat AS REAL) < " + minLat + " AND CAST(l1.lat AS REAL) > " + maxLat + " " +
               "ORDER BY " + order + " " + ascdesc + ";";



System.out.println(query);
        ResultSet results = statement.executeQuery(query);

        while (results.next()) {
            SearchResult searchResults = new SearchResult();

            searchResults.stationId = results.getString("Station Number");
            searchResults.name = results.getString("Station Name");
            searchResults.region = results.getString("Region");
            searchResults.lat = results.getString("Latitude");
            searchResults.metric = results.getString("Metric");
            StationDetails.add(searchResults);
           
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Close Error: " + e.getMessage());
        }
    }

    return StationDetails;
}
public void saveToRegionSummary(String region, String metric, double avgMetric, int stationCount) {
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String colMetricAvg = "\"" + metric + "Average\"";

        // Create table
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS RegionSummary (
                region TEXT PRIMARY KEY,
                StationCount INTEGER
            );
        """;
        statement.executeUpdate(createTableSQL);

        // Check if avg column exists
        ResultSet rs = statement.executeQuery("PRAGMA table_info(RegionSummary);");
        boolean hasAvgCol = false;

        while (rs.next()) {
            String col = rs.getString("name");
            if (col.equals(metric + "Average")) hasAvgCol = true;
        }

        if (!hasAvgCol) {
            statement.executeUpdate("ALTER TABLE RegionSummary ADD COLUMN " + colMetricAvg + " REAL;");
        }

        // Insert/update
        String sql = "INSERT INTO RegionSummary (region, StationCount, " + colMetricAvg + ") VALUES (?, ?, ?) " +
                     "ON CONFLICT(region) DO UPDATE SET " +
                     "StationCount = excluded.StationCount, " +
                     colMetricAvg + " = excluded." + colMetricAvg + ";";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, region);
            pstmt.setInt(2, stationCount);
            pstmt.setDouble(3, avgMetric);
            pstmt.executeUpdate();
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error in saveToRegionSummary: " + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Connection close error: " + e.getMessage());
        }
    }
}

public ArrayList<RegionSummaryRow> getAllRegionSummaryRows() {
    ArrayList<RegionSummaryRow> rows = new ArrayList<>();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String query = "SELECT * FROM RegionSummary;";
        ResultSet results = statement.executeQuery(query);

        while (results.next()) {
            String region = results.getString("region");
            RegionSummaryRow row = new RegionSummaryRow(region);

            for (int i = 1; i <= results.getMetaData().getColumnCount(); ++i) {
                String colName = results.getMetaData().getColumnName(i);
                if (!colName.equalsIgnoreCase("region")) {
                    row.metricStats.put(colName, results.getString(colName));
                }
            }

            rows.add(row);
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("Error reading RegionSummary: " + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    return rows;
}
public void clearRegionSummaryTable() {
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String dropTableSQL = "DROP TABLE IF EXISTS RegionSummary;";
        statement.executeUpdate(dropTableSQL);

        statement.close();
    } catch (SQLException e) {
        System.err.println("Error clearing RegionSummary: " + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Connection close error: " + e.getMessage());
        }
    }
}
public String getRegionSummaryAsCSV() {
    StringBuilder csv = new StringBuilder();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        ResultSet results = statement.executeQuery("SELECT * FROM RegionSummary;");
        int columnCount = results.getMetaData().getColumnCount();

        // Header row
        for (int i = 1; i <= columnCount; i++) {
            csv.append(results.getMetaData().getColumnName(i));
            if (i < columnCount) csv.append(",");
        }
        csv.append("\n");

        // Data rows
        while (results.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String value = results.getString(i);
                if (value != null) value = value.replace("\"", "\"\"");
                csv.append("\"").append(value).append("\"");
                if (i < columnCount) csv.append(",");
            }
            csv.append("\n");
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("Error exporting RegionSummary CSV: " + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Connection close error in export: " + e.getMessage());
        }
    }

    return csv.toString();
}
public ArrayList<String> getWeatherStations() {
    ArrayList<String> stations = new ArrayList<>();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String query = "SELECT name FROM Location ORDER BY name ASC;";



        ResultSet results = statement.executeQuery(query);

        while (results.next()) {
            String name = results.getString("name");

            
            stations.add(name);
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Close Error: " + e.getMessage());
        }
    }

    return stations;
}
// Method to get Station Names for Dropdowns
public ArrayList<String> getStationNames() {
    // Create ArrayList
    ArrayList<String> stationNames = new ArrayList<>();

    // Setup the variable for the JDBC connection
    Connection connection = null;

    try {
        // Connect to JDBC database
        connection = DriverManager.getConnection(DATABASE);

        // Prepare a new SQL Query & Set a timeout
        Statement statement = connection.createStatement();
        // put in a timeout incase the db is not running
        statement.setQueryTimeout(30);

        // The SQL Query to be executed 
        String query = """
                SELECT name FROM Location;
                """;
        // Put the SQL results into a result set
        ResultSet results = statement.executeQuery(query);

        // Process all of the results
        while (results.next()) {
            //Column Lookup
            String station = results.getString("name");

            // Add results to ArrayList
            stationNames.add(station);
        }
        // Close statement
        statement.close();
    } catch (SQLException e) {
    // If there is an error, lets just pring the error
    System.err.println(e.getMessage());
    } finally {
    // Safety code to cleanup
    try {
        if (connection != null) {
            connection.close();
        }
    } catch (SQLException e) {
        // connection close failed.
        System.err.println(e.getMessage());
    }
    }

    // Return ArrayList
    return stationNames;
}

public ArrayList<SearchResult> getSearch2BResults(String metric, String minId, String maxId, String dateStart, String dateEnd) {
    ArrayList<SearchResult> SearchResults = new ArrayList<SearchResult>();
    
    // Setup the variable for the JDBC connection
    Connection connection = null;

    try {
        // Connect to JDBC database
        connection = DriverManager.getConnection(DATABASE);

        // Prepare a new SQL Query & Set a timeout
        Statement statement = connection.createStatement();
        // put in a timeout incase the db is not running
        statement.setQueryTimeout(30);

        // The SQL Query to be executed 
        String query =  
        "SELECT Location, Site, DMY, " + metric + " FROM AAT NATURAL JOIN Location WHERE (Location = Site) AND" +
            "(Site >= " + minId + " AND Site <= " + maxId + ") AND (" + metric + " IS NOT NULL AND TRIM(" + metric + ") != '') AND" +
            "(DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "')" +
        "UNION ALL SELECT Location, Site, DMY, " + metric + " FROM AET NATURAL JOIN Location WHERE (Location = Site) AND" +
            "(Site >= " + minId + " AND Site <= " + maxId + ") AND (" + metric + " IS NOT NULL AND TRIM(" + metric + ") != '') AND" +
            "(DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "')" +
        "UNION ALL SELECT Location, Site, DMY, " + metric + " FROM NSW NATURAL JOIN Location WHERE (Location = Site) AND" +
            "(Site >= " + minId + " AND Site <= " + maxId + ") AND (" + metric + " IS NOT NULL AND TRIM(" + metric + ") != '') AND" +
            "(DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "')" +
        "UNION ALL SELECT Location, Site, DMY, " + metric + " FROM NT NATURAL JOIN Location WHERE (Location = Site) AND" +
            "(Site >= " + minId + " AND Site <= " + maxId + ") AND (" + metric + " IS NOT NULL AND TRIM(" + metric + ") != '') AND" +
            "(DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "')" +
        "UNION ALL SELECT Location, Site, DMY, " + metric + " FROM QLD NATURAL JOIN Location WHERE (Location = Site) AND" +
            "(Site >= " + minId + " AND Site <= " + maxId + ") AND (" + metric + " IS NOT NULL AND TRIM(" + metric + ") != '') AND" +
            "(DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "')" +
        "UNION ALL SELECT Location, Site, DMY, " + metric + " FROM SA NATURAL JOIN Location WHERE (Location = Site) AND" +
            "(Site >= " + minId + " AND Site <= " + maxId + ") AND (" + metric + " IS NOT NULL AND TRIM(" + metric + ") != '') AND" +
            "(DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "')" +
        "UNION ALL SELECT Location, Site, DMY, " + metric + " FROM TAS NATURAL JOIN Location WHERE (Location = Site) AND" +
            "(Site >= " + minId + " AND Site <= " + maxId + ") AND (" + metric + " IS NOT NULL AND TRIM(" + metric + ") != '') AND" +
            "(DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "')" +
        "UNION ALL SELECT Location, Site, DMY, " + metric + " FROM VIC NATURAL JOIN Location WHERE (Location = Site) AND" +
            "(Site >= " + minId + " AND Site <= " + maxId + ") AND (" + metric + " IS NOT NULL AND TRIM(" + metric + ") != '') AND" +
            "(DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "')" +
        "UNION ALL SELECT Location, Site, DMY, " + metric + " FROM WA NATURAL JOIN Location WHERE (Location = Site) AND" +
            "(Site >= " + minId + " AND Site <= " + maxId + ") AND (" + metric + " IS NOT NULL AND TRIM(" + metric + ") != '') AND" +
            "(DMY BETWEEN '" + dateStart + "' AND '" + dateEnd + "')" +
        "ORDER BY Site;";

                        
        // Put the SQL results into a result set
        ResultSet results = statement.executeQuery(query);

        // Process all of the results
        while (results.next()) {
            // Create SearchResult object
            SearchResult searchResults = new SearchResult();

            // Add data to object
            searchResults.stationId = results.getString("Location");
            searchResults.date = results.getString("DMY");
            searchResults.metric = results.getString(metric);

            // Add search results to the ArrayList
            SearchResults.add(searchResults);
        }

        // Close the statement because we are done with it
        statement.close();
    } catch (SQLException e) {
        // If there is an error, lets just pring the error
        System.err.println(e.getMessage());
    } finally {
        // Safety code to cleanup
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    // Return ArrayList
    return SearchResults;
}

public void saveMetricToSummary(String metric, String minId, String maxId, String dateStart, String dateEnd, ArrayList<SearchResult> results) {
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        // Sanitize column name by quoting it
        String columnName = "\"" + metric + "Total\"";

        // Ensure the SummaryTable exists
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS MetricSummary (
                State VARCHAR PRIMARY KEY
            );
        """;
        statement.executeUpdate(createTableSQL);

        // Add the column if it doesn't exist
        String checkColumnSQL = "PRAGMA table_info(MetricSummary);";
        ResultSet rs = statement.executeQuery(checkColumnSQL);
        boolean columnExists = false;

        while (rs.next()) {
            String existingCol = rs.getString("name");
            if (existingCol.equals(metric + "Total")) {
                columnExists = true;
                break;
            }
        }

        if (!columnExists) {
            String addColumnSQL = "ALTER TABLE MetricSummary ADD COLUMN " + columnName + " REAL;";
            statement.executeUpdate(addColumnSQL);
        }

        for (SearchResult result : results) {
            String insert = "INSERT INTO MetricSummary (State, " + columnName + ") " + 
                            "SELECT State, " + result.metric + " FROM Location WHERE (Site = " + result.stationId + ") " + 
                            "ON CONFLICT (State) DO UPDATE SET " + columnName + " = COALESCE(" + columnName + ", 0) + " + result.metric + ";";

            statement.executeQuery(insert);
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error in saveToSummary: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Connection close error in saveToSummary: " + e.getMessage());
        }
    }
}

public ArrayList<MetricSummaryRow> getMetricSummaryRows() {
    ArrayList<MetricSummaryRow> rows = new ArrayList<>();
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String query = "SELECT * FROM MetricSummary;";
        ResultSet results = statement.executeQuery(query);

        while (results.next()) {
            String state = results.getString("State");
            MetricSummaryRow row = new MetricSummaryRow(state);

            for (int i = 1; i <= results.getMetaData().getColumnCount(); ++i) {
                String colName = results.getMetaData().getColumnName(i);
                if (!colName.equalsIgnoreCase("State")) {
                    row.metricTotal.put(colName, results.getString(colName));
                }
            }

            rows.add(row);
        }

        statement.close();
    } catch (SQLException e) {
        System.err.println("Error reading MetricSummary: " + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    return rows;
}

public void clearMetricSummaryTable() {
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String dropTableSQL = "DROP TABLE IF EXISTS MetricSummary;";
        statement.executeUpdate(dropTableSQL);

        statement.close();
    } catch (SQLException e) {
        System.err.println("Error clearing MetricSummary:" + e.getMessage());
    } finally {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Connection close error in clearSummaryTable: " + e.getMessage());
        }
    }
}

// PageST3C Search method
public ArrayList<SearchResult> getSearch3CResults(String station, String metric, String startDate, String endDate) {
    // Create ArrayList
    ArrayList<SearchResult> SearchResults = new ArrayList<SearchResult>();
    // Get list of metrics
    ArrayList<String> metrics = getMetrics();
    metrics.remove(metric);

    // Setup the variable for the JDBC connection
    Connection connection = null;

    try {
        // Connect to JDBC database
        connection = DriverManager.getConnection(DATABASE);

        // Prepare a new SQL Query & Set a timeout
        Statement statement = connection.createStatement();
        // put in a timeout incase the db is not running
        statement.setQueryTimeout(30);

        // The first SQL Query to be executed
        statement.executeUpdate("DROP VIEW IF EXISTS SelectedStation;");
        statement.executeUpdate("CREATE VIEW SelectedStation AS SELECT site, state FROM Location WHERE name = '" + station + "';");
        ResultSet results1 = statement.executeQuery("SELECT * FROM SelectedStation;");

        // Process results from first query
        String stationId = null;
        String stationState = null;

        if (results1.next()) {
            stationId = results1.getString("site");
            stationState = results1.getString("state");

            if (stationState.contains(".")) {
                stationState = stationState.replace(".", "");
            }
        } else {
            // Handle case when station is not found
            throw new SQLException("Station not found in Location table.");
        }

        // Second Query
        String query2 = "WITH" +
                        " DateRange AS (" +
                        "SELECT" +
                            " date((julianday('" + endDate + "') + julianday('" + startDate + "')) / 2) AS midDate" +
                        ")," +
                        " UserMetric AS (" +
                        "SELECT '" + metric + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metric + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metric + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metric + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metric + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metric + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metric + " IS NOT NULL" +
                        ")," +
                        " AllMetrics AS (" +
                        "SELECT '" + metrics.get(0) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(0) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(0) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(0) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(0) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(0) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(0) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(1) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(1) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(1) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(1) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(1) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(1) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(1) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(2) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(2) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(2) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(2) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(2) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(2) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(2) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(3) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(3) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(3) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(3) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(3) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(3) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(3) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(4) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(4) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(4) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(4) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(4) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(4) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(4) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(5) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(5) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(5) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(5) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(5) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(5) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(5) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(6) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(6) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(6) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(6) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(6) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(6) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(6) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(7) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(7) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(7) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(7) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(7) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(7) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(7) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(8) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(8) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(8) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(8) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(8) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(8) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(8) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(9) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(9) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(9) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(9) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(9) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(9) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(9) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(10) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(10) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(10) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(10) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(10) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(10) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(10) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(11) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(11) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(11) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(11) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(11) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(11) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(11) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(12) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(12) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(12) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(12) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(12) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(12) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(12) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(13) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(13) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(13) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(13) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(13) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(13) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(13) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(14) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(14) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(14) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(14) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(14) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(14) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(14) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(15) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(15) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(15) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(15) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(15) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(15) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(15) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(16) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(16) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(16) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(16) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(16) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(16) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(16) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(17) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(17) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(17) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(17) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(17) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(17) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(17) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(18) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(18) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(18) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(18) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(18) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(18) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(18) + " IS NOT NULL" +
                        " UNION ALL " +
                        "SELECT '" + metrics.get(19) + "' AS metric," +
                        " SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(19) + " AS REAL) END) AS SUM_early," +
                        " SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(19) + " AS REAL) END) AS SUM_late," +
                        " ROUND(((SUM(CASE WHEN DMY BETWEEN (SELECT midDate FROM DateRange) AND '" + endDate + "' THEN CAST(" + metrics.get(19) + " AS REAL) END) -" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(19) + " AS REAL) END)) /" + 
                        "        SUM(CASE WHEN DMY BETWEEN '" + startDate + "' AND (SELECT midDate FROM DateRange) THEN CAST(" + metrics.get(19) + " AS REAL) END)) * 100.0, 2)" + 
                        "        AS percent_change" + 
                        " FROM " + stationState + " WHERE location = '" + stationId + "' AND " + metrics.get(19) + " IS NOT NULL" +
                        ")," +
                        " WithPercentChange AS (" +
                        "SELECT metric, SUM_early, SUM_late," +
                        " ROUND(((SUM_late - SUM_early) / SUM_early) * 100.0, 2) AS percent_change" +
                        " FROM AllMetrics" +
                        " WHERE SUM_early IS NOT NULL AND SUM_early != 0" +
                        ")," +
                        " MaxPositive AS (" +
                        "SELECT * FROM WithPercentChange ORDER BY percent_change DESC LIMIT 1" +
                        ")," +
                        " MaxNegative AS (" +
                        "SELECT * FROM WithPercentChange ORDER BY percent_change ASC LIMIT 1" +
                        ")," +
                        " NeutralChange AS (" +
                        "SELECT * FROM WithPercentChange ORDER BY ABS(percent_change) ASC LIMIT 1" +
                        ")" +
                        " SELECT * FROM UserMetric" +
                        " UNION ALL" +
                        " SELECT * FROM MaxPositive" +
                        " UNION ALL" +
                        " SELECT * FROM MaxNegative" +
                        " UNION ALL" +
                        " SELECT * FROM NeutralChange;";

        // Results into result set
        ResultSet result2 = statement.executeQuery(query2);

        // Process results
        while (result2.next()) {
            // Create SearchResult object
            SearchResult searchResults = new SearchResult();

            // Add result to Object
            searchResults.metric = result2.getString("metric");
            searchResults.sumEarly = result2.getString("SUM_early");
            searchResults.sumLate = result2.getString("SUM_late");
            searchResults.percentChange = result2.getString("percent_change");

            // Add search result to the ArrayList
            SearchResults.add(searchResults);
        }

        // Close the statement because we are done with it
        statement.close();

    } catch (SQLException e) {
        // If there is an error, lets just pring the error
        System.err.println(e.getMessage());
    } finally {
        // Safety code to cleanup
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    // Return ArrayList
    return SearchResults;
}
public String getStationID(String name) {
    String stationId = null;
    Connection connection = null;

    try {
        connection = DriverManager.getConnection(DATABASE);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        String query = "SELECT Site FROM Location WHERE Name = '" + name + "';";
        ResultSet results = statement.executeQuery(query);

        if (results.next()) {
            stationId = results.getString("Site");
        }statement.close();
    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Close Error: " + e.getMessage());
        }
    }

    return stationId;
}
public ArrayList<SearchResult> getSimilarStations(String stationID, String metric, String start1, String end1, String start2, String end2, int limit) {
    ArrayList<SearchResult> results = new ArrayList<>();

    String query =
    "WITH CombinedData AS (" +
    "  SELECT Location, DMY, CAST(" + metric + " AS REAL) AS Value FROM AAT " +
    "  UNION ALL SELECT Location, DMY, CAST(" + metric + " AS REAL) FROM AET " +
    "  UNION ALL SELECT Location, DMY, CAST(" + metric + " AS REAL) FROM NSW " +
    "  UNION ALL SELECT Location, DMY, CAST(" + metric + " AS REAL) FROM NT " +
    "  UNION ALL SELECT Location, DMY, CAST(" + metric + " AS REAL) FROM QLD " +
    "  UNION ALL SELECT Location, DMY, CAST(" + metric + " AS REAL) FROM SA " +
    "  UNION ALL SELECT Location, DMY, CAST(" + metric + " AS REAL) FROM TAS " +
    "  UNION ALL SELECT Location, DMY, CAST(" + metric + " AS REAL) FROM VIC " +
    "  UNION ALL SELECT Location, DMY, CAST(" + metric + " AS REAL) FROM WA " +
    "), " +

    "Averages AS (" +
    "  SELECT Location, " +
    "    CASE " +
    "      WHEN DMY BETWEEN '" + start1 + "' AND '" + end1 + "' THEN 'Period1' " +
    "      WHEN DMY BETWEEN '" + start2 + "' AND '" + end2 + "' THEN 'Period2' " +
    "    END AS Period, " +
    "    AVG(Value) AS AvgValue " +
    "  FROM CombinedData " +
    "  WHERE DMY BETWEEN '" + start1 + "' AND '" + end2 + "' " +
    "  GROUP BY Location, Period " +
    "), " +

    "Combined_Columns AS (" +
    "  SELECT Location, " +
    "    MAX(CASE WHEN Period = 'Period1' THEN AvgValue END) AS AvgPeriod1, " +
    "    MAX(CASE WHEN Period = 'Period2' THEN AvgValue END) AS AvgPeriod2 " +
    "  FROM Averages " +
    "  GROUP BY Location " +
    "), " +

    "percentChange AS (" +
    "  SELECT Location, AvgPeriod1, AvgPeriod2, " +
    "    ((AvgPeriod2 - AvgPeriod1) / AvgPeriod1) * 100.0 AS PercentageChange " +
    "  FROM Combined_Columns " +
    "  WHERE AvgPeriod1 IS NOT NULL AND AvgPeriod2 IS NOT NULL AND AvgPeriod1 != 0 " +
    ") " +

    "SELECT L.Name, C.Location, " +
    "  ROUND(C.AvgPeriod1, 2) AS \"Avg Period 1\", " +
    "  ROUND(C.AvgPeriod2, 2) AS \"Avg Period 2\", " +
    "  ROUND(C.PercentageChange, 2) AS \"% Change\", " +
    "  ROUND(C.PercentageChange - Ref.PercentageChange, 2) AS \"Diff from Ref\" " +
    "FROM percentChange C " +
    "JOIN Location L ON C.Location = L.Site " +
    "JOIN (" +
    "  SELECT ((AvgPeriod2 - AvgPeriod1) / AvgPeriod1) * 100.0 AS PercentageChange " +
    "  FROM Combined_Columns WHERE Location = '" + stationID + "'" +
    ") Ref " +
    "ORDER BY ABS(C.PercentageChange - Ref.PercentageChange) " +
    "LIMIT " + limit + ";";

    try (Connection conn = DriverManager.getConnection(DATABASE);
         Statement stmt = conn.createStatement();
         ResultSet result = stmt.executeQuery(query)) {

        while (result.next()) {
            String name = result.getString("Name");
            String id = result.getString("Location");
            String avg1 = result.getString("Avg Period 1");
            String avg2 = result.getString("Avg Period 2");
            String percent = result.getString("% Change");
            String diff = result.getString("Diff from Ref");

            SearchResult row = new SearchResult(id, name, avg1, avg2, percent, diff);
            results.add(row);
        }

    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
    }

    return results;
}

    public ArrayList<SearchResult> get3bResults(String metric, String startDate, String endDate, int limit) throws SQLException {
        ArrayList<SearchResult> results = new ArrayList<>();
        
        ArrayList<String> metrics = getMetrics();

        Connection conn = DriverManager.getConnection(DATABASE);
        Statement stmt = conn.createStatement();

        String startYear = startDate.split("\\-")[0];
        String endYear = endDate.split("\\-")[0];
        String midYear = String.format("%d", (Integer.parseInt(endYear) + Integer.parseInt(startYear)) / 2);

        String query =
        "WITH CombinedData AS (" + //
        "    SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM AAT" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM AET" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM NSW" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM NT" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM QLD" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM SA" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM TAS" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM VIC" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM WA" + //
        ")" + //
        "SELECT SUM(" + metric + ") AS TotalMetric FROM CombinedData WHERE (Year BETWEEN '" + startYear + "'  AND '" + midYear + "');";

        ResultSet rs = stmt.executeQuery(query);

        float metricFirst = 0, metricSecond = 0;

        while (rs.next()) {
            metricFirst = Float.parseFloat(rs.getString("TotalMetric"));
        }

        query =
        "WITH CombinedData AS (" + //
        "    SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM AAT" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM AET" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM NSW" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM NT" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM QLD" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM SA" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM TAS" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM VIC" + //
        "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metric + " FROM WA" + //
        ")" + //
        "SELECT SUM(" + metric + ") AS TotalMetric FROM CombinedData WHERE (Year BETWEEN '" + midYear + "'  AND '" + endYear + "');";
        
        rs = stmt.executeQuery(query);

        while (rs.next()) {
            metricSecond = Float.parseFloat(rs.getString("TotalMetric"));
        }

        float metricDif = metricSecond - metricFirst;
        float metricPercChange = (metricDif / metricFirst) * 100;

        SearchResult sr = new SearchResult();

        sr.metric = metric;
        sr.avgPeriod1 = String.format("%.2f", metricFirst);
        sr.avgPeriod2 = String.format("%.2f", metricSecond);
        sr.diffFromRef = String.format("%.2f (selected)", 0.0);
        sr.percentChange = String.format("%.2f %%", metricPercChange);
        sr.sumEarly = startYear;
        sr.sumLate = endYear;
        sr.date = midYear;

        results.add(sr);

        int cap = limit + 1 > metrics.size() ? metrics.size() : limit + 1;

        for (int i = 0; i < cap; i++) {
            if (metrics.get(i).equals(metric)) {
                continue;
            }

            query =
            "WITH CombinedData AS (" + //
            "    SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM AAT" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM AET" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM NSW" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM NT" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM QLD" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM SA" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM TAS" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM VIC" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM WA" + //
            ")" + //
            "SELECT SUM(" + metrics.get(i) + ") AS TotalMetric FROM CombinedData WHERE (Year BETWEEN '" + startYear + "'  AND '" + midYear + "');";

            rs = stmt.executeQuery(query);

            float first = 0, second = 0;

            while (rs.next()) {
                first = Float.parseFloat(rs.getString("TotalMetric"));
            }

            query =
            "WITH CombinedData AS (" + //
            "    SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM AAT" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM AET" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM NSW" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM NT" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM QLD" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM SA" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM TAS" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM VIC" + //
            "    UNION ALL SELECT Location, STRFTIME('%Y', DMY) as Year, " + metrics.get(i) + " FROM WA" + //
            ")" + //
            "SELECT SUM(" + metrics.get(i) + ") AS TotalMetric FROM CombinedData WHERE (Year BETWEEN '" + midYear + "'  AND '" + endYear + "');";

            rs = stmt.executeQuery(query);

            while (rs.next()) {
                second = Float.parseFloat(rs.getString("TotalMetric"));
            }

            float dif = second - first;
            float percChange = (dif / first) * 100;

            float difFromMetric = metricPercChange - percChange;

            sr = new SearchResult();

            sr.metric = metrics.get(i);
            sr.avgPeriod1 = String.format("%.2f", first);
            sr.avgPeriod2 = String.format("%.2f", second);
            sr.diffFromRef = String.format("%.2f", difFromMetric);
            sr.percentChange = String.format("%.2f %%", percChange);
            sr.sumEarly = startYear;
            sr.sumLate = endYear;
            sr.date = midYear;

            results.add(sr);
        }

        return results;
    }

}
