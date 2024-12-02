package com.lge.connected.domain.bookmark.application;

import com.lge.connected.domain.bookmark.entity.Bookmark;
import com.lge.connected.domain.bookmark.repository.BookmarkRepository;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.repository.VideoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    public Bookmark getBookmarkByVideoId(Long videoId, Long userId) {
        return bookmarkRepository.findByVideoIdAndUserId(videoId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 북마크가 존재하지 않습니다."));
    }

    public void addBookmarkByVideoId(Long videoId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 비디오가 존재하지 않습니다."));

        Bookmark bookmark = Bookmark.builder()
                .video(video)
                .user(user)
                .build();

        bookmarkRepository.save(bookmark);
    }

    public List<Bookmark> getAllBookmarkByUser(Long userId) {
        return bookmarkRepository.findAllByUserId(userId);
    }
}
