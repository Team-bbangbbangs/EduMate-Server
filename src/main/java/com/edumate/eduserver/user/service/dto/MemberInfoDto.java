package com.edumate.eduserver.user.service.dto;

public record MemberInfoDto(
        String nickName,
        String emailAddress
) {
    public static MemberInfoDto of(final String nickName, final String emailAddress) {
        return new MemberInfoDto(nickName, emailAddress);
    }
}
