package com.lge.connected.domain.user.application;

import com.lge.connected.domain.bookmark.entity.Bookmark;
import com.lge.connected.domain.bookmark.repository.BookmarkRepository;
import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.user.dto.UserInfoResponseDto;
import com.lge.connected.domain.user.dto.UserSignupRequest;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.comment.repository.CommentRepository;
import com.lge.connected.global.jwt.CustomUserDetails;
import java.util.List;
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
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;

    public List<Comment> getAllComments(Long id) {
        return commentRepository.findByUserId(id);
    }

    public List<Bookmark> getAllBookmarkByUser(Long userId) {
        return bookmarkRepository.findAllByUserId(userId);
    }

    @Transactional
    public boolean signup(UserSignupRequest request) {
        User user = request.toEntity();
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }

        userRepository.save(user);
        return true;
    }

    public UserInfoResponseDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        return UserInfoResponseDto.of(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        return new CustomUserDetails(user);
    }
}
