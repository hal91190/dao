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
    statement.execute("CREATE TABLE employees(firstname VARCHAR(40), lastname VARCHAR(40) PRIMARY KEY, birthdate DATE)");
    statement.execute("CREATE TABLE functions(name VARCHAR(40) PRIMARY KEY, employee VARCHAR(40) REFERENCES employees(lastname))");
    //TODO créer les tables pour les numéros de téléphone
  }

  @BeforeEach
  public void setup() throws SQLException {
    frodon = new Employee.Builder("Frodon", "Sacquet", LocalDate.of(1987, 6, 12)).build();
    Statement statement = connection.createStatement();
    statement.execute("DELETE FROM functions");
    statement.execute("DELETE FROM employees");
  }

  @Test
  public void createTest() {
    Dao<Employee> employeeDAO = new EmployeeJdbcDao(connection);
    assertTrue(employeeDAO.create(frodon));
    assertEquals(Optional.of(frodon), employeeDAO.read("Sacquet"));
  }

  @Test
  public void updateTest() {
    Employee frodon2 = new Employee.Builder("Frodon", "Sacquet", LocalDate.of(1987, 6, 21)).build();
    Dao<Employee> employeeDAO = new EmployeeJdbcDao(connection);
    assertTrue(employeeDAO.create(frodon));
    assertTrue(employeeDAO.update(frodon2));
    assertEquals(Optional.of(frodon2), employeeDAO.read("Sacquet"));
  }

  @Test
  public void deleteTest() {
    Dao<Employee> employeeDAO = new EmployeeJdbcDao(connection);
    assertTrue(employeeDAO.create(frodon));
    employeeDAO.delete(frodon);
    assertTrue(employeeDAO.read("Sacquet").isEmpty());
  }
}
