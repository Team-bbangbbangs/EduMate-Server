package com.edumate.eduserver.studentrecord.facade.response;

import java.util.List;

public record StudentRecordAICreateResponse(
        String description1,
        String description2,
        String description3
) {
    public static StudentRecordAICreateResponse of(final List<String> description) {
        return new StudentRecordAICreateResponse(description.getFirst(), description.get(1), description.getLast());
    }
}
