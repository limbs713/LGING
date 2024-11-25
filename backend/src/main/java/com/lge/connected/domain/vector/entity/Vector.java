package com.lge.connected.domain.vector.entity;

import com.lge.connected.domain.user.entity.User;
import com.lge.connected.domain.vector.exception.VectorErrorCode;
import com.lge.connected.domain.vector.exception.VectorException;
import com.lge.connected.domain.video.entity.Comment;
import com.lge.connected.domain.video.entity.Video;
import com.lge.connected.domain.video.entity.VideoGenre;
import com.lge.connected.global.util.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Vector {
    private static final List<String> ALL_GENRES = Arrays.asList(
            "Action", "Comedy", "Drama", "SF", "Romance", "Thriller", "Horror", "Animation", "Crime", "Adventure", "War"
    );

    private final List<Double> elements;

    public double cosineSimilarity(Vector other) {
        if (elements.size() != other.elements.size()) {
            throw new VectorException(VectorErrorCode.NOT_SAME_LENGTH);
        }

        Double dotProduct = 0.0;
        Double normA = 0.0;
        Double normB = 0.0;

        for (int i = 0; i < elements.size(); i++) {
            dotProduct += elements.get(i) * other.elements.get(i);
            normA += Math.pow(elements.get(i), 2);
            normB += Math.pow(other.elements.get(i), 2);
        }

        if (normA == 0 || normB == 0) {
            throw new VectorException(VectorErrorCode.VECTOR_ZERO_NORM);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    public static Vector fromString(String vectorString) {
        List<Double> elements = List.of(vectorString.replace("[", "").replace("]", "").split(","))
                .stream().map(String::trim).map(Double::parseDouble).collect(Collectors.toList());
        return new Vector(elements);
    }

    //GENRE -> VECTOR
    public static Vector fromGenres(Map<String, Double> preferenceMap) {
        List<Double> elements = ALL_GENRES.stream()
                .map(pref -> preferenceMap.getOrDefault(pref, 0.0))
                .collect(Collectors.toList());
        return new Vector(elements);
    }

    public static List<String> getTrueGenreElements(Vector vector) {
        List<String> trueGenres = new ArrayList<>();
        for (int i = 0; i < vector.elements.size(); i++) {
            if (vector.elements.get(i) == 1.0) {
                if (i < ALL_GENRES.size()) {
                    trueGenres.add(ALL_GENRES.get(i));
                } else {
                    break;
                }
            }
        }
        return trueGenres;
    }

    public static String inputGenreVector(List<String> inputGenre) {
        // 벡터의 초기값을 0.0으로 설정
        Double[] genreVector = new Double[ALL_GENRES.size()];
        Arrays.fill(genreVector, 0.0);

        // 입력된 장르에 대해 해당 인덱스의 값을 1.0으로 설정
        for (String genre : inputGenre) {
            int index = getGenreIndex(genre);
            if (index == -1) {
                throw new CustomException(VectorErrorCode.UNAVAILABLE_INPUT);
            }
            genreVector[index] = 1.0;
        }

        // 벡터를 문자열 형식으로 변환
        String result = Arrays.stream(genreVector)
                .map(String::valueOf)
                .collect(Collectors.joining(", ", "[", "]"));

        return result;
    }


    public static int getGenreIndex(String genre) {
        int idx = ALL_GENRES.indexOf(genre);
        if (idx == -1) {
            throw new CustomException(VectorErrorCode.GENRE_INDEX_NOT_EXIST);
        } else {
            return idx;
        }
    }

    public static Vector mergeVectors(Vector genreVector, Vector moodVector) {
        List<Double> mergedElements = new ArrayList<>();
        mergedElements.addAll(genreVector.getElements());
        mergedElements.addAll(moodVector.getElements());
        return new Vector(mergedElements);
    }

    public static Vector applyRate(User user, Vector preferenceVector) {
        // 기존 벡터의 요소를 복사하여 수정 가능한 리스트 생성
        List<Double> updatedElements = new ArrayList<>(preferenceVector.getElements());

        // 1. 댓글(Comment)을 통해 평점 데이터를 장르별로 분류
        Map<String, List<Double>> genreRatingsMap = new HashMap<>();

        for (Comment comment : user.getComments()) {
            Double rating = comment.getRating();  // 사용자가 매긴 평점
            Video video = comment.getVideo();    // 해당 평점의 비디오
            VideoGenre videoGenre = video.getVideoGenre(); // 비디오의 장르 정보

            List<String> genres = Vector.getTrueGenreElements(videoGenre.getGenreVector());

            for (String genre : genres) {
                genreRatingsMap.putIfAbsent(genre, new ArrayList<>());
                genreRatingsMap.get(genre).add(rating);
            }
        }

        // 2. 각 장르에 대해 평점 평균을 계산하고 -1~1 범위로 변환 및 벡터에 반영
        for (Map.Entry<String, List<Double>> entry : genreRatingsMap.entrySet()) {
            String genre = entry.getKey();
            List<Double> ratings = entry.getValue();

            // 평점 평균 계산
            double averageRating = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            // 0~5 점을 -1~1로 변환
            double adjustedRating = (averageRating - 2.5) / 2.5;

            // 장르 인덱스를 찾고 기존 벡터에 가중치 적용
            int genreIndex = Vector.getGenreIndex(genre);
            updatedElements.set(genreIndex, updatedElements.get(genreIndex) + adjustedRating);
        }

        // 3. 업데이트된 벡터 반환
        return new Vector(updatedElements);
    }





}
