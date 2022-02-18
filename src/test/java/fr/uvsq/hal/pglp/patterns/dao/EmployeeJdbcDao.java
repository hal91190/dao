package fr.uvsq.hal.pglp.patterns.dao;

import fr.uvsq.hal.pglp.patterns.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * La classe <code>EmployeeJdbcDao</code> implémente la persistance des personnels avec JDBC.
 *
 * @author hal
 * @version 2022
 */
public class EmployeeJdbcDao implements Dao<Employee> {
  private Connection connection;

  public EmployeeJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public boolean create(Employee objet) {
    try {
      PreparedStatement psInsert = connection.prepareStatement("INSERT INTO employees VALUES(?, ?, ?)");
      psInsert.setString(1, objet.getFirstname());
      psInsert.setString(2, objet.getLastname());
      psInsert.setDate(3, Date.valueOf(objet.getBirthDate()));
      psInsert.executeUpdate();

      psInsert = connection.prepareStatement("INSERT INTO functions VALUES(?, ?)");
      for (String function : objet.getFunctions()) {
        psInsert.setString(1, function);
        psInsert.setString(2, objet.getLastname());
        psInsert.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public Optional<Employee> read(String identifier) {
    Employee employee = null;
    try {
      PreparedStatement psInsert = connection.prepareStatement("SELECT * FROM employees WHERE lastname = ?");
      psInsert.setString(1, identifier);
      ResultSet rs = psInsert.executeQuery();
      if (rs.next()) {
        employee = new Employee.Builder(rs.getString(1), rs.getString(2), rs.getDate(3).toLocalDate()).build();

        psInsert = connection.prepareStatement("SELECT * FROM functions WHERE employee = ?");
        psInsert.setString(1, identifier);
        rs = psInsert.executeQuery();
        List<String> functions = new ArrayList<>();
        while (rs.next()) {
          functions.add(rs.getString(1));
        }
        employee.setFunctions(functions);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
    return Optional.ofNullable(employee);
  }

  @Override
  public boolean update(Employee objet) {
    try {
      PreparedStatement ps = connection.prepareStatement("UPDATE employees SET firstname = ?, birthdate = ? WHERE lastname = ?");
      ps.setString(1, objet.getFirstname());
      ps.setDate(2, Date.valueOf(objet.getBirthDate()));
      ps.setString(3, objet.getLastname());
      ps.executeUpdate();

      ps = connection.prepareStatement("DELETE FROM functions WHERE employee = ?");
      ps.setString(1, objet.getLastname());
      ps.executeUpdate();
      ps = connection.prepareStatement("INSERT INTO functions VALUES(?, ?)");
      for (String function : objet.getFunctions()) {
        ps.setString(1, function);
        ps.setString(2, objet.getLastname());
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public void delete(Employee objet) {
    try {
      PreparedStatement ps = connection.prepareStatement("DELETE FROM functions WHERE employee = ?");
      ps.setString(1, objet.getLastname());
      ps.executeUpdate();
      ps = connection.prepareStatement("DELETE FROM employees WHERE lastname = ?");
      ps.setString(1, objet.getLastname());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
