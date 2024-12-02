package com.lge.connected.domain.bookmark.presentation;

import com.lge.connected.domain.bookmark.application.BookmarkService;
import com.lge.connected.domain.bookmark.entity.Bookmark;
import com.lge.connected.global.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/{videoId}")
    public ResponseEntity<Bookmark> getBookmarkByVideoId(@PathVariable Long videoId) {
        Long userId = SecurityUtils.getCurrentMemberId();
        return ResponseEntity.ok(bookmarkService.getBookmarkByVideoId(videoId,userId));
    }

    @PostMapping("/{videoId}")
    public ResponseEntity<String> addBookmarkByVideoId(@PathVariable Long videoId) {
        Long userId = SecurityUtils.getCurrentMemberId();
        bookmarkService.addBookmarkByVideoId(videoId, userId);
        return ResponseEntity.ok("Bookmark added successfully");
    }



}
