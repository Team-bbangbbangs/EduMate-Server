package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;

public record StudentRecordPromptResponse(
        Member member,
        StudentRecordType recordType,
        String inputPrompt
) {
    public static StudentRecordPromptResponse of(final Member member, final StudentRecordType recordType,
                                                 final String inputPrompt) {
        return new StudentRecordPromptResponse(member, recordType, inputPrompt);
    }
}
