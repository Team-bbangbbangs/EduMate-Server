package com.edumate.eduserver.studentrecord.facade.response;

import java.util.List;

public record StudentNameResponse(
        List<String>  studentNames
) {
    public static StudentNameResponse of(final List<String> studentNames) {
        return new StudentNameResponse(studentNames);
    }
}
