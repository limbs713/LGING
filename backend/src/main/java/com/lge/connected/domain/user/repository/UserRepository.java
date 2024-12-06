package com.lge.connected.domain.user.repository;


import java.util.Optional;
import com.lge.connected.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByUsername(String username);
    public Boolean existsDistinctByLoginId(String loginId);
}
