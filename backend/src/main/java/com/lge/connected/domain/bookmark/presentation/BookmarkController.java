package com.lge.connected.domain.bookmark.presentation;

import com.lge.connected.domain.bookmark.application.BookmarkService;
import com.lge.connected.domain.bookmark.entity.Bookmark;

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

    @GetMapping("/{userId}/{videoId}")
    public ResponseEntity<Bookmark> getBookmarkByVideoId(@PathVariable Long userId, @PathVariable Long videoId) {
        return ResponseEntity.ok(bookmarkService.getBookmarkByVideoId(videoId,userId));
    }

    @PostMapping("/{userId}/{videoId}")
    public ResponseEntity<String> addBookmarkByVideoId(@PathVariable Long userId, @PathVariable Long videoId) {
        bookmarkService.addBookmarkByVideoId(videoId, userId);
        return ResponseEntity.ok("Bookmark added successfully");
    }



}
