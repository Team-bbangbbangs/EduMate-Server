package com.edumate.eduserver.user.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    TEACHER("TEACHER"),
    PENDING_TEACHER("PENDING_TEACHER"),
    ADMIN("ADMIN");

    private final String name;
}
