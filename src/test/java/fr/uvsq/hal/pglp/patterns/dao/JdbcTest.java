package fr.uvsq.hal.pglp.patterns.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcTest {
  private static final String DB_URL = "jdbc:derby:memory:testdb;create=true";
  private static Connection connection;

  @BeforeAll
  public static void setupBeforeAll() throws SQLException {
    connection = DriverManager.getConnection(DB_URL);
    Statement statement = connection.createStatement();
    statement.execute("CREATE TABLE personnes(id int, nom VARCHAR(20))");
  }

  @BeforeEach
  public void setup() throws SQLException {
    Statement statement = connection.createStatement();
    statement.execute("TRUNCATE TABLE personnes");
    PreparedStatement psInsert = connection.prepareStatement("INSERT INTO personnes VALUES(?, ?)");
    psInsert.setInt(1, 1); psInsert.setString(2, "Heywood Floyd"); psInsert.executeUpdate();
    psInsert.setInt(1, 2); psInsert.setString(2, "David Bowman"); psInsert.executeUpdate();
    psInsert.setInt(1, 3); psInsert.setString(2, "Frank Poole"); psInsert.executeUpdate();
    psInsert.setInt(1, 4); psInsert.setString(2, "Hal 9000"); psInsert.executeUpdate();
  }

  @Test
  public void selectTest() throws SQLException {
    assertDbState(List.of("David Bowman", "Frank Poole", "Hal 9000", "Heywood Floyd"));
  }

  @Test
  public void insertTest() throws SQLException {
    PreparedStatement psInsert = connection.prepareStatement("INSERT INTO personnes VALUES(?, ?)");
    psInsert.setInt(1, 5); psInsert.setString(2, "Sal 9000");
    int nbAffectedRows = psInsert.executeUpdate();
    assertEquals(1, nbAffectedRows);

    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery("SELECT nom FROM personnes WHERE id = 5");
    rs.next();
    assertEquals("Sal 9000", rs.getString(1));
  }

  @Test
  public void updateTest() throws SQLException {
    PreparedStatement psUpdate = connection.prepareStatement("UPDATE personnes SET nom = ? WHERE id = ?");
    psUpdate.setString(1, "Sal 9000"); psUpdate.setInt(2, 4);
    int nbAffectedRows = psUpdate.executeUpdate();
    assertEquals(1, nbAffectedRows);

    assertDbState(List.of("David Bowman", "Frank Poole", "Heywood Floyd", "Sal 9000"));
  }

  @Test
  public void deleteTest() throws SQLException {
    PreparedStatement psDelete = connection.prepareStatement("DELETE FROM personnes WHERE id = ?");
    psDelete.setInt(1, 4);
    int nbAffectedRows = psDelete.executeUpdate();
    assertEquals(1, nbAffectedRows);

    assertDbState(List.of("David Bowman", "Frank Poole", "Heywood Floyd"));
  }

  private static void assertDbState(List<String> expectedPersons) throws SQLException {
    List<String> persons = new ArrayList<>();
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery("SELECT id, nom FROM personnes ORDER BY nom");
    while (rs.next()) {
      persons.add(rs.getString(2));
    }
    assertEquals(expectedPersons, persons);
  }
}
