package com.lge.connected.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class MovieRequestDTO {
    private Map<String, Double> genrePreferences;
}
