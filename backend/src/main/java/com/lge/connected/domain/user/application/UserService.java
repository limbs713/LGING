package com.lge.connected.domain.user.application;

import com.lge.connected.domain.bookmark.entity.Bookmark;
import com.lge.connected.domain.bookmark.repository.BookmarkRepository;
import com.lge.connected.domain.comment.entity.Comment;
import com.lge.connected.domain.comment.repository.CommentRepository;
import com.lge.connected.domain.like.repository.LikeRepository;
import com.lge.connected.domain.user.dto.OnBoardingDTO;
import com.lge.connected.domain.user.dto.UserInfoResponseDto;
import com.lge.connected.domain.user.dto.UserResponseDTO;
import com.lge.connected.domain.user.dto.UserSignupRequest;
import com.lge.connected.domain.user.entity.Archive;
import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.user.entity.UserGenre;
import com.lge.connected.domain.user.exception.UserErrorCode;
import com.lge.connected.domain.user.exception.UserGenreErrorCode;
import com.lge.connected.domain.user.repository.ArchiveRepository;
import com.lge.connected.domain.user.repository.UserGenreRepository;
import com.lge.connected.domain.user.repository.UserRepository;
import com.lge.connected.domain.vector.entity.Vector;
import com.lge.connected.domain.video.dto.VideoResponseDTO;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.entity.VideoGenre;
import com.lge.connected.domain.video.exception.ArchiveErrorCode;
import com.lge.connected.domain.video.exception.VideoGenreErrorCode;
import com.lge.connected.domain.video.repository.VideoGenreRepository;
import com.lge.connected.domain.video.repository.VideoRepository;
import com.lge.connected.global.config.jwt.CustomUserDetails;
import com.lge.connected.global.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserGenreRepository userGenreRepository;
    private final ArchiveRepository archiveRepository;
    private final VideoRepository videoRepository;
    private final VideoGenreRepository videoGenreRepository;
    private final LikeRepository likeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public Long signup(UserSignupRequest request) {
        User user = request.toEntity(bCryptPasswordEncoder.encode(request.getPassword()));
        isDuplicate(user);
        return userRepository.save(user).getId();
    }

    private void isDuplicate(User user) throws CustomException {
        if (userRepository.existsDistinctByLoginId(user.getLoginId())) {
            throw new CustomException(UserErrorCode.LOGINID_ALREADY_EXIST);
        }
    }


//    public boolean login(UserLoginRequest request) {
//        User user = request.toEntity();
//        User found_user = userRepository.findByLoginId(user.getLoginId()).orElseThrow(
//                () -> new IllegalArgumentException("User not found")
//        );
//
//        return user.getPassword().equals(found_user.getPassword());
//    }

    @Transactional
    public List<VideoResponseDTO> recommendVideos(Long userId, Long num) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));
        UserGenre userGenre;
        Archive archive;

        if (user.getLatestArchiveId() == null) {
            archive = archiveRepository.findLatestArchiveByUser(user).orElseThrow(() -> new CustomException(ArchiveErrorCode.ARCHIVE_NOT_EXIST));
            user.saveLatestArchiveId(archive.getArchiveId());
            userRepository.save(user);
            userGenre = userGenreRepository.findLatestGenreByUser(user).orElseThrow(() -> new CustomException(UserGenreErrorCode.USER_GENRE_NOT_EXIST));
        } else {
            archive = archiveRepository.findById(user.getLatestArchiveId()).orElseThrow(() -> new CustomException(ArchiveErrorCode.ARCHIVE_NOT_EXIST));
            userGenre = userGenreRepository.findById(archive.getUserGenre().getUserGenreId()).orElseThrow(() -> new CustomException(UserGenreErrorCode.USER_GENRE_NOT_EXIST));
        }

        Vector userVector = Vector.applyRate(user, userGenre.getGenreVector());


        List<Video> allVideos = videoRepository.findAll();
        List<Vector> allVideoVectors = new ArrayList<>();

        for (Video video : allVideos) {
            VideoGenre venueGenre = videoGenreRepository.findByVideo(video).orElseThrow(() -> new CustomException(VideoGenreErrorCode.VIDEO_GENRE_NOT_EXIST));
            allVideoVectors.add(venueGenre.getGenreVector());
        }
        List<Vector> recommendVideoVectors = allVideoVectors.stream()
                .sorted(Comparator.comparingDouble(v -> {
                    try {
                        return -userVector.cosineSimilarity(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return Double.MIN_VALUE;
                    }
                }))
                .limit(num)
                .collect(Collectors.toList());

        return recommendVideoVectors.stream()
                .map(venueVector -> {
                    int index = allVideoVectors.indexOf(venueVector);
                    Video video = allVideos.get(index);

                    List<Double> genreElements = venueVector.getElements().subList(0, 10);
                    Vector genreVector = new Vector(genreElements);
                    List<String> trueGenreElements = Vector.getTrueGenreElements(genreVector);

                    List<String> tagList = new ArrayList<>(trueGenreElements);

                    return VideoResponseDTO.builder()
                            .tagList(tagList)
                            .videoId(video.getId())
                            .title(video.getTitle())
                            .subtitle(video.getSubtitle())
                            .likes(video.getLikes())
                            .comments(video.getComments())
                            .views(video.getViews())
                            .doesLike(likeRepository.findByUserVideo(user, video).isPresent())
                            .source(video.getSource())
                            .thumbnail(video.getThumbnail())
                            .description(video.getDescription())
                            .build();
                })
                .collect(Collectors.toList());
    }


    public List<Comment> getAllComments(Long id) {
        return commentRepository.findByUserId(id);
    }

    public List<Bookmark> getAllBookmarkByUser(Long userId) {
        return bookmarkRepository.findAllByUserId(userId);
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

    @Transactional
    public UserResponseDTO saveOnBoarding(Long userId, OnBoardingDTO onBoardingDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_EXIST));

        user.saveOnBoarding(onBoardingDTO.getUsername(), onBoardingDTO.getNickname(), onBoardingDTO.getGender(), onBoardingDTO.getAge());
        userRepository.save(user);
        return UserResponseDTO.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .age(user.getAge())
                .build();
    }


}
