package fr.uvsq.hal.pglp.patterns.dao;

import fr.uvsq.hal.pglp.patterns.Employee;

import java.util.Optional;

/**
 * La classe <code>EmployeeJdbcDao</code> implémente la persistance des personnels avec JDBC.
 *
 * @author hal
 * @version 2022
 */
public class EmployeeJdbcDao implements Dao<Employee> {
  @Override
  public boolean create(Employee objet) {
    return false;
  }

  @Override
  public Optional<Employee> read(String identifier) {
    return Optional.empty();
  }

  @Override
  public boolean update(Employee objet) {
    return false;
  }

  @Override
  public void delete(Employee objet) {

  }
}
