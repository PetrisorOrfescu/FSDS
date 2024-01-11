package com.example.a1.repositories;


import com.example.a1.entities.Uuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<Uuser, UUID> {

    Optional<Uuser> findByUsername(String username);
    Boolean existsByUsername(String username);
    @Query(value = "SELECT u " +
            "FROM Uuser u " +
            "WHERE u.username = :username " +
            "AND u.password = :password")
    Optional<Uuser> findUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);


}
