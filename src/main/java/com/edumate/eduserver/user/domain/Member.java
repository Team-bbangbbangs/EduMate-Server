package com.edumate.eduserver.user.domain;

import static java.util.UUID.randomUUID;

import com.edumate.eduserver.BaseEntity;
import com.edumate.eduserver.subject.domain.Subject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private School school;

    @Column(columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Column(nullable = false)
    private String memberUuid;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedAt;

    @Builder
    private Member(final Subject subject, final String email, final String password, final String nickname,
                   final School school, final Role role, final boolean isDeleted) {
        this.subject = subject;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.school = school;
        this.role = role;
        this.isDeleted = isDeleted;
        this.memberUuid = randomUUID().toString();
    }

    public static Member create(final Subject subject, final String email, final String password,
        final String nickname, final School school, final Role role) {
        return com.edumate.eduserver.user.domain.Member.builder()
            .subject(subject)
            .email(email)
            .password(password)
            .nickname(nickname)
            .school(school)
            .role(role)
            .isDeleted(false)
            .build();
    }
}
