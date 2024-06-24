package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.token.Token;
import com.example.DentistryManagement.core.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    @Query(value = """
      SELECT t from Token t inner join Client u\s
      on t.user.userID = u.userID\s
      where u.userID = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(String id);
}
