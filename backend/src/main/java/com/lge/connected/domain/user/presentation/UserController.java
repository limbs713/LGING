package com.lge.connected.domain.user.presentation;

import com.lge.connected.domain.bookmark.entity.Bookmark;
import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.user.application.UserService;
import com.lge.connected.domain.user.dto.UserInfoResponseDto;
import com.lge.connected.domain.user.dto.UserSignupRequest;
import com.lge.connected.global.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignupRequest request) {
        if (userService.signup(request)) {
            return ResponseEntity.ok("User signed up successfully");
        }

        return ResponseEntity.badRequest().body("User already exists");
    }

    @GetMapping
    public ResponseEntity<UserInfoResponseDto> getUserInfo() {
        Long id = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>>getAllComments(){
        Long id = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(userService.getAllComments(id));
    }


    @GetMapping("/bookmark")
    public ResponseEntity<List<Bookmark>> getAllBookmarkByUser(){
        Long userId = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(userService.getAllBookmarkByUser(userId));
    }

}
