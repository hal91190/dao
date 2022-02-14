package fr.uvsq.hal.pglp.patterns.dao;

import java.util.Optional;

/**
 * La classe <code>DAO</code> est la classe de base pour les DAO de l'application.
 *
 * @author hal
 * @version 2022
 */
public interface Dao<T> {
  boolean create(T objet);

  Optional<T> read(String identifier);

  boolean update(T objet);

  void delete(T objet);
}
