package com.lge.connected.domain.user.repository;

import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.entity.UserGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {
    @Query("SELECT ug FROM UserGenre ug WHERE ug.user = :user ORDER BY ug.createdAt DESC LIMIT 1")
    Optional<UserGenre> findLatestGenreByUser(@Param("user") User user);

    @Query("SELECT ug FROM UserGenre ug WHERE ug.user = :user ORDER BY ug.createdAt DESC")
    List<UserGenre> findAllByUser(@Param("user") User user);

    boolean existsByUser(User user);

    void deleteByUser(User user);
}
