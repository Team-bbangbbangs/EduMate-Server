package com.edumate.eduserver.studentrecord.domain;

import com.edumate.eduserver.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class StudentRecordDetail extends BaseEntity {

    @Id
    @Column(name = "student_record_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_student_record_id", nullable = false)
    private RecordMetadata recordMetadata;

    @Column(nullable = false)
    private String studentNumber;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int byteCount;

    public void updateContent(final String description, final int byteCount) {
        this.description = description;
        this.byteCount = byteCount;
    }

    public void update(final String studentNumber, final String studentName, final String description,
                       final int byteCount) {
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.description = description;
        this.byteCount = byteCount;
    }

    @Builder(access = AccessLevel.PRIVATE)
    private StudentRecordDetail(final RecordMetadata recordMetadata, final String studentNumber,
                                final String studentName, final String description, final int byteCount) {
        this.recordMetadata = recordMetadata;
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.description = description;
        this.byteCount = byteCount;
    }

    public static StudentRecordDetail create(final RecordMetadata recordMetadata, final String studentNumber,
                                             final String studentName, final String description, final int byteCount) {
        return StudentRecordDetail.builder()
                .recordMetadata(recordMetadata)
                .studentNumber(studentNumber)
                .studentName(studentName)
                .description(description)
                .byteCount(byteCount)
                .build();
    }
}
