package fr.uvsq.hal.pglp.patterns.dao;

import fr.uvsq.hal.pglp.patterns.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DaoFactoryTest {
  private static final String DIRECTORY_PREFIX = "dao_tests";
  private static Path tmpDirectory;

  private static final String DB_URL = "jdbc:derby:memory:testdb;create=true";
  private static Connection connection;

  @BeforeAll
  public static void beforeAll() throws IOException, SQLException {
    tmpDirectory = Files.createTempDirectory(DIRECTORY_PREFIX);

    connection = DriverManager.getConnection(DB_URL);
  }

  @Test
  public void creationAPartirDeSerializedDaoFactory() {
    Dao<Employee> employeeDao = new SerializedDaoFactory(tmpDirectory).getEmployeeDao();
    assertTrue(employeeDao instanceof EmployeeSerializedDao);
  }

  @Test
  public void creationAPartirDeJdbcDaoFactory() {
    Dao<Employee> employeeDao = new JdbcDaoFactory(connection).getEmployeeDao();
    assertTrue(employeeDao instanceof EmployeeJdbcDao);
  }

  @Test
  public void creationAPartirDeDaoFactory() throws SQLException, IOException {
    DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.DaoType.SERIALIZED);
    Dao<Employee> employeeDao = daoFactory.getEmployeeDao();
    assertTrue(employeeDao instanceof EmployeeSerializedDao);

    daoFactory = DaoFactory.getDaoFactory(DaoFactory.DaoType.JDBC);
    employeeDao = daoFactory.getEmployeeDao();
    assertTrue(employeeDao instanceof EmployeeJdbcDao);
  }

  @Test
  public void aNullDaoTypeShouldGenerateNpe() {
    assertThrows(NullPointerException.class, () -> DaoFactory.getDaoFactory(null));
  }
}
