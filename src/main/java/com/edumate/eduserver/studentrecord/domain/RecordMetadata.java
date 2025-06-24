package com.edumate.eduserver.studentrecord.domain;

import com.edumate.eduserver.member.domain.Member;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordMetadata {

    @Id
    @Column(name = "record_metadata_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentRecordType studentRecordType;

    @Column(nullable = false)
    private String semester;

    @Builder(access = AccessLevel.PRIVATE)
    private RecordMetadata(final Member member, final StudentRecordType studentRecordType, final String semester) {
        this.member = member;
        this.studentRecordType = studentRecordType;
        this.semester = semester;
    }

    public static RecordMetadata create(final Member member, final StudentRecordType studentRecordType, final String semester) {
        return RecordMetadata.builder()
                .member(member)
                .studentRecordType(studentRecordType)
                .semester(semester)
                .build();
    }
}
