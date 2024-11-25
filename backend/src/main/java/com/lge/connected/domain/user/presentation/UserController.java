package com.lge.connected.domain.user.presentation;

import com.lge.connected.domain.user.application.UserService;
import com.lge.connected.domain.user.dto.UserLoginRequest;
import com.lge.connected.domain.user.dto.UserSignupRequest;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.global.config.jwt.SecurityUtils;
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
    public ResponseEntity<String> signup(@RequestBody UserSignupRequest request) {
        System.out.println("Signup request: " + request);
        if (userService.signup(request)>0) {
            return ResponseEntity.ok("User signed up successfully");
        }

        return ResponseEntity.badRequest().body("User already exists");
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
//        if (userService.login(request)) {
//            return ResponseEntity.ok("User logged in successfully");
//        }
//
//        return ResponseEntity.badRequest().body("Invalid credentials");
//    }

    @GetMapping("/{userId}/recommend")
    public ResponseEntity<List<VideoResponseDTO>> recommendVenues(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.recommendVideos(userId, 5L));
    }


}
