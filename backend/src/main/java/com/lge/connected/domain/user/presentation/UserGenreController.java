package com.lge.connected.domain.user.presentation;

import com.lge.connected.domain.user.application.UserGenreService;
import com.lge.connected.domain.user.dto.MovieRequestDTO;
import com.lge.connected.domain.user.dto.UserResponseDTO;
import com.lge.connected.domain.user.dto.UserVectorResponseDTO;
import com.lge.connected.domain.vector.dto.GenreRequestDTO;
import com.lge.connected.global.config.jwt.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-genre")
@RequiredArgsConstructor
public class UserGenreController {
    private final UserGenreService userGenreService;



    @DeleteMapping("/{userId}/{userGenreId}")
    public ResponseEntity<UserVectorResponseDTO> deleteGenrePreference(@PathVariable Long userId, @PathVariable Long userGenreId) {
        return ResponseEntity.ok(userGenreService.deleteGenreVector(userId, userGenreId));
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<UserVectorResponseDTO>> getAllGenrePreference(@PathVariable Long userId) {
        return ResponseEntity.ok(userGenreService.getAllGenreVector(userId));
    }

    @GetMapping("/{userId}/latest")
    public ResponseEntity<UserVectorResponseDTO> getLatestGenrePreference(@PathVariable Long userId) {
        return ResponseEntity.ok(userGenreService.getLatestGenreVector(userId));
    }

    // 선호 영화 장르 입력받기
    @PostMapping("/onboarding/{userId}")
    public ResponseEntity<UserVectorResponseDTO> saveMoviePreference(@PathVariable Long userId, @RequestBody MovieRequestDTO movieRequestDTO) {
        return ResponseEntity.ok(userGenreService.addGenreVector(userId, movieRequestDTO.getGenrePreferences()));
    }


}
