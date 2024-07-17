package com.example.mariate.repository;

import com.example.mariate.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {

    public boolean existsByEmailAndPassword(String email, String password);

    public UsersEntity findByEmail(String email);

    public UsersEntity findByNickname(String nickname);

    public boolean existsByNickname(String nickname);

}
