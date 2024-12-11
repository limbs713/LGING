package com.lge.connected.domain.user.presentation;

import com.lge.connected.domain.bookmark.entity.Bookmark;
import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.user.application.UserService;
import com.lge.connected.domain.user.dto.*;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Boolean> signup(@RequestBody UserSignupRequest request) {
        if (userService.signup(request)>0) {
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.ok(false);
    }

    // 추천 받기
    @GetMapping("/recommend/{userId}")
    public ResponseEntity<List<VideoResponseDTO>> recommendVenues(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.recommendVideos(userId, 5L));
    }

    // 닉네임, 나이, 성별 입력받기
    @PostMapping("/onboarding/{userId}")
    public ResponseEntity<UserResponseDTO> saveOnBoarding(@PathVariable Long userId, @RequestBody OnBoardingDTO onBoardingDTO) {
        return ResponseEntity.ok(userService.saveOnBoarding(userId, onBoardingDTO));
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<List<Comment>>getAllComments(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getAllComments(userId));
    }

    @GetMapping("/{userId}/onboarding")
    public ResponseEntity<Boolean> getOnboarding(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getOnboarding(userId));
    }


    @GetMapping("/{userId}/bookmarks")
    public ResponseEntity<List<VideoResponseDTO>> getAllBookmarkByUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getAllBookmarkByUser(userId));
    }

    @GetMapping("/{userId}/likes")
    public ResponseEntity<List<VideoResponseDTO>> getLikedVideos(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getLikedVideos(userId));
    }


}
