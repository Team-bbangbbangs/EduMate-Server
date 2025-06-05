package com.edumate.eduserver.studentrecord.domain;

import com.edumate.eduserver.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
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
    private MemberStudentRecord memberStudentRecord;

    @Column(nullable = false)
    private String studentNumber;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int byteCount;
}
