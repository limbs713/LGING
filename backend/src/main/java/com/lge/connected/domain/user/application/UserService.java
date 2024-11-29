package com.lge.connected.domain.user.application;

import com.lge.connected.domain.user.dto.UserInfoResponse;
import com.lge.connected.domain.user.dto.UserSignupRequest;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.global.jwt.CustomUserDetails;
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

    public UserInfoResponse getUserInfo(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        return UserDetailResponse.of(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        return new CustomUserDetails(user);
    }
}
