package user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user.application.UserService;
import user.dto.UserLoginRequest;
import user.dto.UserSignupRequest;

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

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        if (userService.login(request)) {
            return ResponseEntity.ok("User logged in successfully");
        }

        return ResponseEntity.badRequest().body("Invalid credentials");
    }
}
