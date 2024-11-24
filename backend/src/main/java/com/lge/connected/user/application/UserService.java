package com.lge.connected.user.application;

import com.lge.connected.global.jwt.CustomUserDetails;
import com.lge.connected.user.dto.UserSignupRequest;
import com.lge.connected.user.entity.User;
import com.lge.connected.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Transactional
    public boolean signup(UserSignupRequest request) {
        User user = request.toEntity();
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }

        userRepository.save(user);
        return true;
    }

//    public boolean login(UserLoginRequest request) {
//        User user = userRepository.findByUsername(request.getUserName()).orElseThrow(
//                () -> new IllegalArgumentException("User not found")
//        );
//
//        return user.getPassword().equals(request.getPassword());
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        return new CustomUserDetails(user);
    }
}
