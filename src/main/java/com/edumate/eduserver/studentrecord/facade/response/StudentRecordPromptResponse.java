package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.studentrecord.domain.StudentRecordType;

public record StudentRecordPromptResponse(
        StudentRecordType recordType,
        String inputPrompt
) {
    public static StudentRecordPromptResponse of(final StudentRecordType recordType,
                                                 final String inputPrompt) {
        return new StudentRecordPromptResponse(recordType, inputPrompt);
    }
}
