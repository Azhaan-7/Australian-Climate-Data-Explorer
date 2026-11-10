package app;

import junit.framework.TestCase;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.zip.ZipFile;

public class PrivacyTest extends TestCase {
    public void testContributorHtmlContainsNoStudentIdentifierField() {
        var member = new PageMission().new GroupMember();
        member.setName("Contributor");
        member.setSubTask("A");
        String html = member.toHtml();
        assertTrue(html.contains("Contributor"));
        assertFalse(html.toLowerCase(Locale.ROOT).contains("student"));
    }

    public void testSeedAndReadmeContainNoStudentIdentifierFields() throws Exception {
        for (String file : new String[]{"database/mission.sql", "README.md"}) {
            String text = Files.readString(Path.of(file));
            assertFalse(file, text.toLowerCase(Locale.ROOT).contains("studentnum"));
            assertFalse(file, java.util.regex.Pattern.compile("(?i)\bs[0-9]{7}\b").matcher(text).find());
        }
    }

    public void testDistributedDatabaseStoresContributorNamesAndTasksOnly() throws Exception {
        Path temporary = Files.createTempFile("climate-privacy-test-", ".db");
        try {
            try (ZipFile archive = new ZipFile("database/climate.db.zip");
                 var input = archive.getInputStream(archive.getEntry("climate.db"))) {
                Files.copy(input, temporary, StandardCopyOption.REPLACE_EXISTING);
            }
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + temporary.toUri() + "?mode=ro");
                 Statement statement = connection.createStatement()) {
                List<String> columns = new ArrayList<>();
                try (ResultSet rows = statement.executeQuery("PRAGMA table_info(GroupMember)")) {
                    while (rows.next()) columns.add(rows.getString("name"));
                }
                assertEquals(Arrays.asList("Name", "SubTask"), columns);
                Set<String> contributors = new HashSet<>();
                try (ResultSet rows = statement.executeQuery("SELECT Name FROM GroupMember")) {
                    while (rows.next()) contributors.add(rows.getString(1));
                }
                assertEquals(new HashSet<>(Arrays.asList("Azhaanul Haque Farooqui", "Kieran Collins", "Sam Virgona")), contributors);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
