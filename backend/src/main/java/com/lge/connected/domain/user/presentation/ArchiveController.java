package com.lge.connected.domain.user.presentation;


import com.lge.connected.domain.user.application.ArchiveService;
import com.lge.connected.domain.user.dto.ArchiveDTO;
import com.lge.connected.domain.user.dto.ArchiveRequestDTO;
import com.lge.connected.domain.user.dto.ArchiveResponseDTO;
import com.lge.connected.domain.user.dto.ArchiveUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/archive")
@RequiredArgsConstructor
public class ArchiveController {
    private final ArchiveService archiveService;

    @PostMapping("/{userId}")
    public ResponseEntity<ArchiveDTO> addArchive(@RequestBody ArchiveRequestDTO archiveRequestDTO, @PathVariable Long userId){
        Long userGenreId = archiveRequestDTO.getUserGenreId();
        return ResponseEntity.ok(archiveService.addPreferenceInArchive(userId, userGenreId));
    }

    @DeleteMapping("/{userId}/{archiveId}")
    public ResponseEntity<ArchiveDTO> deleteArchive(@PathVariable Long archiveId, @PathVariable Long userId){
        return ResponseEntity.ok(archiveService.deletePreferenceInArchive(userId, archiveId));
    }

    @PatchMapping("/{userId}/{archiveId}")
    public ResponseEntity<ArchiveDTO> updateArchive( @PathVariable Long userId, @PathVariable Long archiveId, @RequestBody ArchiveUpdateDTO archiveUpdateDTO) {
        return ResponseEntity.ok(archiveService.updatePreferenceInArchive(userId, archiveId, archiveUpdateDTO));
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<ArchiveResponseDTO>> getArchives(@PathVariable Long userId){
        return ResponseEntity.ok(archiveService.getArchives(userId));
    }

    @PostMapping("/history/{userId}/{archiveId}")
    public ResponseEntity<Long> toGetHistory(@PathVariable Long userId, @PathVariable Long archiveId) {
        return ResponseEntity.ok(archiveService.toGetHistory(userId, archiveId));
    }

}
