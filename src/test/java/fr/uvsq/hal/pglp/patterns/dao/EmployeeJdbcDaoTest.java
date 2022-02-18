package fr.uvsq.hal.pglp.patterns.dao;

import fr.uvsq.hal.pglp.patterns.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * La classe <code>EmployeeJdbcDaoTest</code> ...
 *
 * @author hal
 * @version 2022
 */
public class EmployeeJdbcDaoTest {
  private static final String DB_URL = "jdbc:derby:memory:testdb;create=true";
  private static Connection connection;

  private Employee frodon;

  @BeforeAll
  public static void beforeAll() throws SQLException {
    connection = DriverManager.getConnection(DB_URL);
    Statement statement = connection.createStatement();
    //TODO créer les tables
    statement.execute("CREATE TABLE personnes(id int, nom VARCHAR(20))");
  }

  @BeforeEach
  public void setup() {
    frodon = new Employee.Builder("Frodon", "Sacquet", LocalDate.of(1987, 6, 12)).build();
  }

  @Test
  public void createTest() {
    Dao<Employee> employeeDAO = new EmployeeJdbcDao();
    employeeDAO.create(frodon);
    assertEquals(Optional.of(frodon), employeeDAO.read("Frodon_Sacquet"));
  }

  @Test
  public void updateTest() {
    Employee frodon2 = new Employee.Builder("Frodon", "Sacquet", LocalDate.of(1987, 6, 21)).build();
    Dao<Employee> employeeDAO = new EmployeeJdbcDao();
    employeeDAO.create(frodon);
    employeeDAO.update(frodon2);
    assertEquals(Optional.of(frodon2), employeeDAO.read("Frodon_Sacquet"));
  }

  @Test
  public void deleteTest() {
    Dao<Employee> employeeDAO = new EmployeeJdbcDao();
    employeeDAO.create(frodon);
    employeeDAO.delete(frodon);
    assertTrue(employeeDAO.read("Frodon_Sacquet").isEmpty());
  }
}
