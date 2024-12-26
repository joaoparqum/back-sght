package com.api.casadoconstrutor.sght.repository;

import com.api.casadoconstrutor.sght.model.Solicitacao;
import com.api.casadoconstrutor.sght.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByLogin(String login);

}
