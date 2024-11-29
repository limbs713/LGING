package com.lge.connected.domain.user.presentation;

import com.lge.connected.domain.user.application.UserService;
import com.lge.connected.domain.user.dto.UserSignupRequest;
import com.lge.connected.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
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
    public ResponseEntity<String> getUserInfo() {
        User user = SecurityUtil.getCurrentUser();
        if (userService.login(request)) {
            return ResponseEntity.ok("User logged in successfully");
        }

        return ResponseEntity.badRequest().body("Invalid credentials");
    }
}
