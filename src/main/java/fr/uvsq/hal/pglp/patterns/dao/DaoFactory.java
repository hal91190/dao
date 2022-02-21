package fr.uvsq.hal.pglp.patterns.dao;

import fr.uvsq.hal.pglp.patterns.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * La classe <code>DaoFactory</code> permet de créer des fabriques pour un type de DAO.
 *
 * @author hal
 * @version 2022
 */
public abstract class DaoFactory {
  public enum DaoType { SERIALIZED, JDBC; }

  private static final String DIRECTORY_PREFIX = "dao_tests";
  private static final String DB_URL = "jdbc:derby:memory:testdb;create=true";

  public abstract Dao<Employee> getEmployeeDao();

  public static DaoFactory getDaoFactory(DaoType daoType) throws IOException, SQLException {
    Objects.requireNonNull(daoType);
    if (daoType == DaoType.SERIALIZED) {
      Path tmpDirectory = Files.createTempDirectory(DIRECTORY_PREFIX);

      return new SerializedDaoFactory(tmpDirectory);
    }
    if (daoType == DaoType.JDBC) {
      Connection connection = DriverManager.getConnection(DB_URL);
      return new JdbcDaoFactory(connection);
    }
    return null;
  }
}
