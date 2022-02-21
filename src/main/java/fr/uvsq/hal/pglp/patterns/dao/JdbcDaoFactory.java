package fr.uvsq.hal.pglp.patterns.dao;

import fr.uvsq.hal.pglp.patterns.Employee;

import java.sql.Connection;

/**
 * La classe <code>JdbcDaoFactory</code> permet de créer des DAO basés sur JDBC.
 *
 * @author hal
 * @version 2022
 */
public class JdbcDaoFactory extends DaoFactory {
  private final Connection connection;

  public JdbcDaoFactory(Connection connection) {
    this.connection = connection;
  }

  public Dao<Employee> getEmployeeDao() {
    return new EmployeeJdbcDao(connection);
  }
}
