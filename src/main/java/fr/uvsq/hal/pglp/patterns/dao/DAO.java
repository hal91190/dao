package fr.uvsq.hal.pglp.patterns.dao;

import java.util.Optional;

/**
 * La classe <code>DAO</code> est la classe de base pour les DAO de l'application.
 *
 * @author hal
 * @version 2022
 */
public interface DAO<T> {
  boolean create(T objet);
  Optional<T> read(String identifier);
  T update(T objet);
  void delete(T objet);
}
