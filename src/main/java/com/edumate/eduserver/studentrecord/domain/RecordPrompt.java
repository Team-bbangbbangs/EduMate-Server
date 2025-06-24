package com.edumate.eduserver.studentrecord.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordPrompt {

    @Id
    @Column(name = "record_prompt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudentRecordDetail studentRecordDetail;

    @Column(nullable = false)
    private String prompt;

    @Builder
    private RecordPrompt(final StudentRecordDetail studentRecordDetail, final String prompt) {
        this.studentRecordDetail = studentRecordDetail;
        this.prompt = prompt;
    }

    public static RecordPrompt create(final StudentRecordDetail studentRecordDetail, final String prompt) {
        return RecordPrompt.builder()
                .studentRecordDetail(studentRecordDetail)
                .prompt(prompt)
                .build();
    }
}
