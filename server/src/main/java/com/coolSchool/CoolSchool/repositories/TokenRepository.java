package com.coolSchool.CoolSchool.repositories;

import java.util.List;
import java.util.Optional;

import com.coolSchool.CoolSchool.models.entity.Token;
import com.coolSchool.CoolSchool.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Long> {

  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(Long id);

  List<Token> findAllByUser(User user);

  Optional<Token> findByToken(String token);
}
