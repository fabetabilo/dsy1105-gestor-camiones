package com.log_austral.cl.log_austral.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.log_austral.cl.log_austral.model.Camion;

@Repository
public interface CamionRepository extends JpaRepository<Camion, Integer>{
    /*  CRUD (Create, Read, Update, Delete):
    o save(S entity): Guarda una entidad.
    o findById(ID id): Encuentra una entidad por su ID.
    o existsById(ID id): Verifica si una entidad con un ID dado existe.
    o findAll(): Encuentra todas las entidades.
    o findAllById(Iterable<ID> ids): Encuentra todas las entidades por sus IDs.
    o count(): Cuenta todas las entidades.
    o deleteById(ID id): Borra una entidad por su ID.
    o delete(S entity): Borra una entidad.
    o deleteAll(): Borra todas las entidades.
    Paginación y Ordenación:
    • findAll(Pageable pageable): Encuentra todas las entidades con paginación.
    • findAll(Sort sort): Encuentra todas las entidades con ordenación.
    */

}
