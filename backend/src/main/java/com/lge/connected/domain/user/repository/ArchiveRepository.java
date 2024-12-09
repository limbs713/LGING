package com.lge.connected.domain.user.repository;

import com.lge.connected.domain.user.entity.Archive;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.entity.UserGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    @Query("SELECT a FROM Archive a WHERE a.user = :user ORDER BY a.updatedAt DESC LIMIT 3")
    List<Archive> findByUser(@Param("user") User user);

    boolean existsByUserAndUserGenre(User user, UserGenre userGenre);

    void deleteByUser(User user);

    @Query("SELECT a FROM Archive a WHERE a.user = :user ORDER BY a.updatedAt DESC LIMIT 1")
    Optional<Archive> findLatestArchiveByUser(@Param("user") User user);
}
