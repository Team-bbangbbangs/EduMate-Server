package com.edumate.eduserver.auth.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.edumate.eduserver.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorizationCode {

    @Id
    @Column(name = "authorization_code_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String authorizationCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorizeStatus status;

    private static final int MINUTES_TO_EXPIRE = 10;

    @Builder
    private AuthorizationCode(final Member member, final String authorizationCode, final LocalDateTime createdAt,
                              final LocalDateTime expiredAt, final AuthorizeStatus status) {
        this.member = member;
        this.authorizationCode = authorizationCode;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.status = status;
    }

    public static AuthorizationCode create(final Member member, final String authorizationCode,
                                           final AuthorizeStatus status) {
        return AuthorizationCode.builder()
                .member(member)
                .authorizationCode(authorizationCode)
                .status(status)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(MINUTES_TO_EXPIRE))
                .build();
    }

    public void verified() {
        this.status = AuthorizeStatus.VERIFIED;
    }

    public void fail() {
        this.status = AuthorizeStatus.FAILED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public void updateCode(final String code) {
        this.authorizationCode = code;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusMinutes(MINUTES_TO_EXPIRE);
        this.status = AuthorizeStatus.REVOKED;
    }
}
