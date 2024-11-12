package org.imannuel.moviereservationapi.repository;

import org.imannuel.moviereservationapi.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO m_user (id, username, email, password, role_id) VALUES (:#{#userAccount.id}, :#{#userAccount.username}, :#{#userAccount.email}, :#{#userAccount.password}, :#{#userAccount.role.id})", nativeQuery = true)
    void insertUserAccount(@Param("userAccount") UserAccount userAccount);

    @Query(value = "SELECT id, username, email, password, role_id from m_user where id = :id", nativeQuery = true)
    Optional<UserAccount> findUserAccountById(@Param("id") String id);

    @Query(value = "SELECT id, username, email, password, role_id from m_user where username = :username", nativeQuery = true)
    UserAccount findUserAccountByUsername(@Param("username") String username);

    @Query(value = "SELECT EXISTS(SELECT username from m_user where username = :username)", nativeQuery = true)
    boolean existsUserAccountByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE m_user SET username = :username, email = :email WHERE id = :id", nativeQuery = true)
    void updateUserAccount(String id, String username, String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE m_user SET role_id = :adminRoleId WHERE id = :id", nativeQuery = true)
    void updateRoleToAdmin(String id, Integer adminRoleId);
}
