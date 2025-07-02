package com.edumate.eduserver.member.domain;

import com.edumate.eduserver.common.entity.BaseEntity;
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
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

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

    @Column
    private LocalDateTime verifiedAt;

    private String refreshToken;

    @Column(nullable = false)
    private String memberUuid;

    @Column(nullable = false)
    private boolean isDeleted;

    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(final Subject subject, final String email, final String password, final String nickname,
                   final School school, final Role role, final LocalDateTime verifiedAt, final boolean isDeleted,
                   final LocalDateTime deletedAt, final String memberUuid) {
        this.subject = subject;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.school = school;
        this.role = role;
        this.verifiedAt = verifiedAt;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
        this.memberUuid = memberUuid;
    }

    public static Member create(final Subject subject, final String email, final String password,
                                final String nickname, final School school, final Role role) {
        return Member.builder()
                .subject(subject)
                .email(email)
                .password(password)
                .nickname(nickname)
                .school(school)
                .role(role)
                .isDeleted(false)
                .memberUuid(UUID.randomUUID().toString())
                .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Role.toGrantedAuthorities(Set.of(role));
    }

    public void verifyAsTeacher() {
        this.role = Role.TEACHER;
        this.verifiedAt = LocalDateTime.now();
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public boolean isVerifyTeacher() {
        return this.role != Role.PENDING_TEACHER;
    }

    public void updatePassword(final String password) {
        this.password = password;
    }

    public void restore(final String password, final Subject subject, final School school) {
        this.isDeleted = false;
        this.deletedAt = null;
        this.password = password;
        this.subject = subject;
        this.school = school;
        this.role = Role.PENDING_TEACHER;
    }

    public void updateProfile(final Subject subject, final School school, final String nickname) {
        this.subject = subject;
        this.school = school;
        this.nickname = nickname;
    }
}
