package com.coolSchool.CoolSchool.repository;


import com.coolSchool.CoolSchool.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByTokenValue(String tokenValue);

    @Query("SELECT t FROM Token t WHERE t.expired = false AND t.revoked = false")
    List<Token> getAllUnexpiredTokensForInspection();
}