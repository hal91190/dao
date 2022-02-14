package fr.uvsq.hal.pglp.patterns.dao;

import fr.uvsq.hal.pglp.patterns.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * La classe <code>EmployeeSerializedDAOTest</code> ...
 *
 * @author hal
 * @version 2022
 */
public class EmployeeSerializedDAOTest {
  private static final String DIRECTORY_PREFIX = "dao_tests";
  private static Path tmpDirectory;

  private Employee frodon;

  @BeforeAll
  public static void beforeAll() throws IOException {
    tmpDirectory = Files.createTempDirectory(DIRECTORY_PREFIX);
  }

  @BeforeEach
  public void setup() {
    frodon = new Employee.Builder("Frodon", "Sacquet", LocalDate.of(1987, 6, 12)).build();
  }

  @Test
  public void createTest() {
    EmployeeSerializedDAO employeeDAO = new EmployeeSerializedDAO(tmpDirectory);
    employeeDAO.create(frodon);
    assertEquals(Optional.of(frodon), employeeDAO.read("Frodon_Sacquet"));
  }
}
