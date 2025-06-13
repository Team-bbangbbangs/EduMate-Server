package com.edumate.eduserver.auth.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    ACCESS("ACCESS"),
    REFRESH("REFRESH");

    private final String type;
}
