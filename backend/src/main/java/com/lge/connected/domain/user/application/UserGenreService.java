package com.lge.connected.domain.user.application;

import com.lge.connected.domain.user.dto.UserVectorResponseDTO;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.entity.UserGenre;
import com.lge.connected.domain.user.exception.UserErrorCode;
import com.lge.connected.domain.user.exception.UserGenreErrorCode;
import com.lge.connected.domain.user.repository.UserGenreRepository;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.vector.entity.Vector;
import com.lge.connected.global.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserGenreService {
    private final UserRepository userRepository;
    private final UserGenreRepository userGenreRepository;

    @Transactional
    public UserVectorResponseDTO addGenreVector(Long userId, Map<String, Double> genres) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        Vector preferenceVector = Vector.fromGenres(genres);

        if (Vector.getTrueGenreElements(preferenceVector).size()>4){
            throw new CustomException(UserGenreErrorCode.USER_GENRE_MAX_LIMIT);
        }

        UserGenre userGenre = UserGenre.builder()
                .user(user).genreVectorString(preferenceVector.toString())
                .build();

        userGenreRepository.save(userGenre);
        return UserVectorResponseDTO.builder()
                .vectorString(userGenre.getGenreVectorString())
                .userId(user.getId())
                .vectorId(userGenre.getUserGenreId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .build();
    }


    @Transactional
    public UserVectorResponseDTO deleteGenreVector(Long userId, Long memberGenreId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        UserGenre userGenre = userGenreRepository.findById(memberGenreId).orElseThrow(()->new CustomException((UserGenreErrorCode.USER_GENRE_NOT_EXIST)));
        List<UserGenre> userGenres = userGenreRepository.findAllByUser(user);

        if (userGenres.size() <= 1) {
            throw new CustomException(UserGenreErrorCode.USER_GENRE_ONLY_ONE);
        }

        userGenreRepository.delete(userGenre);

        return UserVectorResponseDTO.builder()
                .vectorString(userGenre.getGenreVectorString())
                .userId(user.getId())
                .vectorId(userGenre.getUserGenreId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .build();
    }

    public List<UserVectorResponseDTO> getAllGenreVector(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        List<UserGenre> userGenres = userGenreRepository.findAllByUser(user);
        return userGenres.stream()
                .map(userGenre -> UserVectorResponseDTO.builder()
                        .userId(user.getId())
                        .vectorId(userGenre.getUserGenreId())
                        .nickname(user.getNickname())
                        .username(user.getUsername())
                        .vectorString(userGenre.getGenreVectorString())
                        .build())
                .collect(Collectors.toList());
    }

    public UserVectorResponseDTO getLatestGenreVector(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        UserGenre userGenre = userGenreRepository.findLatestGenreByUser(user).orElseThrow(()-> new CustomException((UserGenreErrorCode.USER_GENRE_NOT_EXIST)));
        return UserVectorResponseDTO.builder()
                .vectorString(userGenre.getGenreVectorString())
                .userId(user.getId())
                .vectorId(userGenre.getUserGenreId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .build();
    }

}

