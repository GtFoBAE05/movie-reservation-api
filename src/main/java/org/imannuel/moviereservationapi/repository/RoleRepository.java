package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT EXISTS (SELECT name FROM m_role WHERE name = :name)", nativeQuery = true)
    boolean existsRoleByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO m_role(name) VALUES (:name)", nativeQuery = true)
    void insertRole(@Param("name") String name);

    @Query(value = "SELECT id, name FROM m_role WHERE name = :name", nativeQuery = true)
    Optional<Role> findRoleByName(@Param("name") String name);
}
