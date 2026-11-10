package app;

import junit.framework.TestCase;
import java.nio.file.Files;
import java.sql.*;
import java.util.*;

public class QueryOptimisationTest extends TestCase {
    private static final String[] TABLES = {"AAT", "AET", "NSW", "NT", "QLD", "SA", "TAS", "VIC", "WA"};
    private static final String[] METRICS = {"Precipitation", "Evaporation", "MaxTemp", "MinTemp",
        "Humid00", "Humid03", "Humid06", "Humid09", "Humid12", "Humid15", "Humid18", "Humid21",
        "Sunshine", "Okta00", "Okta03", "Okta06", "Okta09", "Okta12", "Okta15", "Okta18", "Okta21"};
    private static boolean initialized;

    protected void setUp() throws Exception {
        if (initialized) return;
        Locale.setDefault(Locale.US);
        var database = Files.createTempFile("climate-query-test-", ".db");
        database.toFile().deleteOnExit();
        System.setProperty("climate.database", "jdbc:sqlite:" + database);
        try (Connection c = DriverManager.getConnection(JDBCConnection.DATABASE);
             Statement s = c.createStatement()) {
            s.execute("CREATE TABLE Metadata(Field)");
            for (String metric : METRICS) s.execute("INSERT INTO Metadata VALUES ('" + metric + "')");
            s.execute("INSERT INTO Metadata VALUES (''), ('   '), (NULL), ('Location'), ('DMY'), ('MinTempQual'), ('RainDaysNum')");
            s.execute("CREATE TABLE Location(site TEXT, name TEXT, state TEXT)");
            s.execute("INSERT INTO Location VALUES ('123', 'Station O''Brien', 'N.S.W.')");
            for (String table : TABLES) s.execute("CREATE TABLE " + table + " (Location, DMY, "
                    + String.join(", ", METRICS) + ")");
            String[] dates = {"1999-12-31", "2000-01-01", "2001-01-01", "2002-12-31", "2003-01-01"};
            int[] values = {1000, 2, 4, 8, 1000};
            for (int d = 0; d < dates.length; d++) {
                StringJoiner row = new StringJoiner(", ");
                row.add("'123'").add("'" + dates[d] + "'");
                for (int i = 0; i < METRICS.length; i++) row.add("'" + (values[d] * (i + 1)) + "'");
                s.execute("INSERT INTO NSW VALUES (" + row + ")");
            }
        }
        initialized = true;
    }

    public void testBlankMetricNamesAreExcluded() {
        assertEquals(Arrays.asList(METRICS), new JDBCConnection().getMetrics());
    }

    public void testMetricComparisonPreservesYearsMidpointOrderingAndFormatting() throws Exception {
        var rows = new JDBCConnection().get3bResults("MinTemp", "2000-06-01", "2002-02-01", 1);
        assertEquals(3, rows.size());
        assertEquals("MinTemp", rows.get(0).metric);
        assertEquals("Precipitation", rows.get(1).metric);
        assertEquals("Evaporation", rows.get(2).metric);
        assertEquals("24.00", rows.get(0).avgPeriod1);
        assertEquals("48.00", rows.get(0).avgPeriod2);
        assertEquals("100.00 %", rows.get(0).percentChange);
        assertEquals("0.00 (selected)", rows.get(0).diffFromRef);
        assertEquals("0.00", rows.get(1).diffFromRef);
        assertEquals("2001", rows.get(0).date);
        assertEquals(21, new JDBCConnection().get3bResults("MinTemp", "2000-01-01", "2002-12-31", 100).size());
    }

    public void testCrossMetricMidpointTiesAndQuotedStationName() {
        var rows = new JDBCConnection().getSearch3CResults("Station O'Brien", "MinTemp", "2001-01-01", "2001-01-01");
        assertEquals(4, rows.size());
        assertEquals("MinTemp", rows.get(0).metric);
        assertEquals("16.0", rows.get(0).sumEarly);
        assertEquals("16.0", rows.get(0).sumLate);
        assertEquals("0.0", rows.get(0).percentChange);
        for (int i = 1; i < rows.size(); i++) assertEquals("Precipitation", rows.get(i).metric);
    }

    public void testCrossMetricDoesNotChangeSchema() throws Exception {
        new JDBCConnection().getSearch3CResults("Station O'Brien", "MinTemp", "2000-01-01", "2002-12-31");
        try (Connection c = DriverManager.getConnection(JDBCConnection.DATABASE);
             Statement s = c.createStatement();
             ResultSet rows = s.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='view'")) {
            assertEquals(0, rows.getInt(1));
        }
    }

    public void testHomepageExtremesPreserveTieOrder() throws Exception {
        var jdbc = new JDBCConnection();
        var minimum = jdbc.getNameAndMinTemp();
        var rain = jdbc.getNameAndMaxRain();
        assertEquals("8.0", minimum.get(1));
        assertEquals("2000-01-01", minimum.get(2));
        assertEquals("1000.0", rain.get(1));
        assertEquals("1999-12-31", rain.get(2));
        JDBCConnection.initializeIndexes();
        assertEquals(minimum, jdbc.getNameAndMinTemp());
        assertEquals(rain, jdbc.getNameAndMaxRain());
    }

    public void testNullAndBlankMeasurementsKeepZeroAndNullSemantics() throws Exception {
        try (Connection c = DriverManager.getConnection(JDBCConnection.DATABASE);
             Statement s = c.createStatement()) {
            s.execute("INSERT INTO Location VALUES ('456', 'Empty measurements', 'N.S.W.')");
            s.execute("INSERT INTO NSW(Location, DMY, MinTemp) VALUES ('456', '2001-01-01', ''), ('456', '2001-01-01', NULL)");
            try {
                var rows = new JDBCConnection().getSearch3CResults("Empty measurements", "MinTemp", "2001-01-01", "2001-01-01");
                assertEquals(1, rows.size());
                assertEquals("0.0", rows.get(0).sumEarly);
                assertEquals("0.0", rows.get(0).sumLate);
                assertNull(rows.get(0).percentChange);
            } finally {
                s.execute("DELETE FROM NSW WHERE Location='456'");
                s.execute("DELETE FROM Location WHERE site='456'");
            }
        }
    }

    public void testHomepageNullAndCrossTableTieHandling() throws Exception {
        try (Connection c = DriverManager.getConnection(JDBCConnection.DATABASE);
             Statement s = c.createStatement()) {
            s.execute("INSERT INTO AAT(Location, DMY, MinTemp, Precipitation) VALUES ('123', '1970-01-01', NULL, '1000')");
            try {
                var jdbc = new JDBCConnection();
                var minimum = jdbc.getNameAndMinTemp();
                var rainfall = jdbc.getNameAndMaxRain();
                assertNull(minimum.get(1));
                assertEquals("1970-01-01", minimum.get(2));
                assertEquals("1970-01-01", rainfall.get(2));
                JDBCConnection.initializeIndexes();
                assertEquals(minimum, jdbc.getNameAndMinTemp());
                assertEquals(rainfall, jdbc.getNameAndMaxRain());
            } finally {
                s.execute("DELETE FROM AAT");
            }
        }
    }

    public void testIndexesAreIdempotentAndPreserveResults() throws Exception {
        var jdbc = new JDBCConnection();
        var before = jdbc.getSearch3CResults("Station O'Brien", "MinTemp", "2000-01-01", "2002-12-31");
        JDBCConnection.initializeIndexes();
        JDBCConnection.initializeIndexes();
        var after = jdbc.getSearch3CResults("Station O'Brien", "MinTemp", "2000-01-01", "2002-12-31");
        assertEquals(before.size(), after.size());
        for (int i = 0; i < before.size(); i++) {
            assertEquals(before.get(i).metric, after.get(i).metric);
            assertEquals(before.get(i).sumEarly, after.get(i).sumEarly);
            assertEquals(before.get(i).sumLate, after.get(i).sumLate);
            assertEquals(before.get(i).percentChange, after.get(i).percentChange);
        }
        try (Connection c = DriverManager.getConnection(JDBCConnection.DATABASE);
             Statement s = c.createStatement();
             ResultSet rows = s.executeQuery("EXPLAIN QUERY PLAN SELECT * FROM NSW WHERE Location='123' AND DMY BETWEEN '2000-01-01' AND '2002-12-31'")) {
            assertTrue(rows.getString("detail").contains("idx_NSW_location_dmy"));
        }
    }
}
