package com.edumate.eduserver.studentrecord.facade.response;

public record StudentRecordAICreateResponse(
        String description
) {
    public static StudentRecordAICreateResponse of(final String description) {
        return new StudentRecordAICreateResponse(description);
    }
}
