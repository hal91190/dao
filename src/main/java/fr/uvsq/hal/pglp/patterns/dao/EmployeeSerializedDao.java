package fr.uvsq.hal.pglp.patterns.dao;

import fr.uvsq.hal.pglp.patterns.Employee;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * La classe <code>EmployeeSerializedDAO</code> permet de charger/sauvegarder un personnel.
 *
 * @author hal
 * @version 2022
 */
public class EmployeeSerializedDao implements Dao<Employee> {
  private final Path directory;

  public EmployeeSerializedDao(Path directory) {
    this.directory = directory;
  }

  @Override
  public boolean create(Employee employee) {
    String filename = employee.getFirstname() + "_" + employee.getLastname();
    Path employeePath = directory.resolve(filename);
    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(employeePath))) {
      oos.writeObject(employee);
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  @Override
  public Optional<Employee> read(String identifier) {
    Path employeePath = directory.resolve(identifier);
    try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(employeePath))) {
      Employee employee = (Employee) ois.readObject();
      return Optional.of(employee);
    } catch (IOException e) {
      // return Optional.empty();
    } catch (ClassNotFoundException e) {
      // return Optional.empty();
    }
    return Optional.empty();
  }

  @Override
  public boolean update(Employee employee) {
    delete(employee);
    return create(employee);
  }

  @Override
  public void delete(Employee employee) {
    String filename = employee.getFirstname() + "_" + employee.getLastname();
    Path employeePath = directory.resolve(filename);
    try {
      Files.deleteIfExists(employeePath);
    } catch (IOException e) {
      // Ignore the error
    }
  }
}
