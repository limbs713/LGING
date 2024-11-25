package com.lge.connected.domain.user.application;

import com.lge.connected.domain.user.dto.ArchiveDTO;
import com.lge.connected.domain.user.dto.ArchiveResponseDTO;
import com.lge.connected.domain.user.dto.ArchiveUpdateDTO;
import com.lge.connected.domain.user.entity.Archive;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.entity.UserGenre;
import com.lge.connected.domain.user.exception.UserErrorCode;
import com.lge.connected.domain.user.exception.UserGenreErrorCode;
import com.lge.connected.domain.user.repository.ArchiveRepository;
import com.lge.connected.domain.user.repository.UserGenreRepository;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.vector.entity.Vector;
import com.lge.connected.domain.video.exception.ArchiveErrorCode;
import com.lge.connected.global.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArchiveService {
    private final ArchiveRepository archiveRepository;
    private final UserRepository userRepository;
    private final UserGenreRepository userGenreRepository;

    @Transactional
    public ArchiveDTO addPreferenceInArchive(Long userId, Long userGenreId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));

        UserGenre userGenre = userGenreRepository.findById(userGenreId).orElseThrow(()-> new CustomException((UserGenreErrorCode.USER_GENRE_NOT_EXIST)));

        boolean exists = archiveRepository.existsByUserAndUserGenre(user, userGenre);
        if (exists) {
            throw new CustomException(ArchiveErrorCode.ARCHIVE_ALREADY_EXIST);
        }
        Archive archive = Archive.builder()
                .userGenre(userGenre)
                .user(user)
                .build();
        Archive newArchive = archiveRepository.save(archive);
        user.saveLatestArchiveId(newArchive.getArchiveId());
        userRepository.save(user);

        ArchiveDTO newarchive = ArchiveDTO.builder()
                .userGenreList(Vector.getTrueGenreElements(archive.getUserGenre().getGenreVector()))
                .updatedAt(archive.getUpdatedAt())
                .userId(user.getId())
                .archiveId(archive.getArchiveId())
                .build();
        return newarchive;
    }

    @Transactional
    public ArchiveDTO deletePreferenceInArchive(Long memberId, Long archiveId) {
        Archive archive = archiveRepository.findById(archiveId).orElseThrow(()->new CustomException(ArchiveErrorCode.ARCHIVE_NOT_EXIST));
        archiveRepository.delete(archive);
        User user = userRepository.findById(memberId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        user.saveLatestArchiveId(null);
        userRepository.save(user);

        return ArchiveDTO.builder()
                .userGenreList(Vector.getTrueGenreElements(archive.getUserGenre().getGenreVector()))
                .updatedAt(archive.getUpdatedAt())
                .userId(archive.getUser().getId())
                .archiveId(archive.getArchiveId())
                .build();
    }


    @Transactional
    public ArchiveDTO updatePreferenceInArchive(Long memberId, Long archiveId, ArchiveUpdateDTO archiveUpdateDTO) {
        Archive archive = archiveRepository.findById(archiveId).orElseThrow(()->new CustomException(ArchiveErrorCode.ARCHIVE_NOT_EXIST));
        Vector genreVector = Vector.fromString(archiveUpdateDTO.getUserGenreVector());

        User user = userRepository.findById(memberId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        user.saveLatestArchiveId(archiveId);

        UserGenre newMemberGenre = UserGenre.builder()
                .user(archive.getUser())
                .genreVector(genreVector)
                .genreVectorString(genreVector.toString())
                .build();


        userGenreRepository.save(newMemberGenre);

        archive.updateArchive(newMemberGenre);

        archiveRepository.save(archive);
        return ArchiveDTO.builder()
                .userGenreList(Vector.getTrueGenreElements(archive.getUserGenre().getGenreVector()))
                .updatedAt(archive.getUpdatedAt())
                .userId(archive.getUser().getId())
                .archiveId(archive.getArchiveId())
                .build();
    }

    public List<ArchiveResponseDTO> getArchives(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        List<Archive> archives = archiveRepository.findByUser(user);


        return archives.stream()
                .map(archive -> {
                    List<String> userGenreList = Vector.getTrueGenreElements(archive.getUserGenre().getGenreVector());

                    return ArchiveResponseDTO.builder()
                            .preferenceList(userGenreList)
                            .userId(archive.getUser().getId())
                            .archiveId(archive.getArchiveId())
                            .updatedAt(archive.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long toGetHistory(Long userId, Long archiveId){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        Archive archive = archiveRepository.findById(archiveId).orElseThrow(()->new CustomException(ArchiveErrorCode.ARCHIVE_NOT_EXIST));
        if(archive.getUser().getId() != user.getId()) throw new CustomException(ArchiveErrorCode.ARCHIVE_MEMBER_NOT_MATCH);

        archive.updateToGetHistory(LocalDateTime.now());
        archiveRepository.save(archive);

        user.saveLatestArchiveId(archiveId);
        userRepository.save(user);
        return user.getLatestArchiveId();
    }

}

