package com.lge.connected.domain.user.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Role {
    TYPE1(0, "USER"),
    TYPE2(1, "ADMIN");

    private int idx;
    private String text;

    Role(int idx, String text) {
        this.idx = idx;
        this.text = text;
    }

    public int getIdx() {
        return idx;
    }

    @JsonValue
    public String getText() {
        return text;
    }

    @JsonCreator
    public static Role fromText(String text) {
        for (Role role : Role.values()) {
            if (role.getText().equals(text)) {
                return role;
            }
        }
        return null;
    }

    public static Role fromIdx(int idx) {
        for (Role role : Role.values()) {
            if (role.getIdx() == idx) {
                return role;
            }
        }
        return null;
    }

}
