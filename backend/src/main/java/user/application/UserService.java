package user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.dto.UserLoginRequest;
import user.dto.UserSignupRequest;
import user.entity.User;
import user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public boolean signup(UserSignupRequest request) {
        User newUser = request.toEntity();
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            return false;
        }

        userRepository.save(newUser);
        return true;
    }

    public boolean login(UserLoginRequest request) {
        User user = request.toEntity();
        User found_user = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        return user.getPassword().equals(found_user.getPassword());
    }
}
