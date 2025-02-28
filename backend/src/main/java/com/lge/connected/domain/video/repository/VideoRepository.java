package com.lge.connected.domain.video.repository;

import com.lge.connected.domain.video.entity.Video;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("SELECT v FROM Video v ORDER BY v.views DESC LIMIT 5")
    List<Video> sortByViewCount();
//
//    @Query("SELECT v FROM Venue v WHERE v.venueId = :venueId")
//    Long deleteByVenueId(@Param("venueId")Long venueId);

}
