package com.edumate.eduserver.common;

import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.RecordTypeNotFoundException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;
import java.util.Arrays;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StudentRecordTypeConverter implements Converter<String, StudentRecordType> {

    @Override
    public StudentRecordType convert(final String input) {
        return Arrays.stream(StudentRecordType.values())
                .filter(type -> type.getValue().equalsIgnoreCase(input.trim()))
                .findFirst()
                .orElseThrow(() -> new RecordTypeNotFoundException(StudentRecordErrorCode.RECORD_TYPE_NOT_FOUND, input.trim()));
    }
}
