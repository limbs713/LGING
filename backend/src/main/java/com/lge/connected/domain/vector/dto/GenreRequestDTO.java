package com.lge.connected.domain.vector.dto;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
public class GenreRequestDTO {
    @NotNull
    private Map<String, Double> genrePreferences;
}
