package com.edumate.eduserver.common;

import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.InvalidRecordTypeException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;
import java.util.Arrays;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StudentRecordTypeConverter implements Converter<String, StudentRecordType> {

    @Override
    public StudentRecordType convert(final String input) {
        String value = input.strip();
        return Arrays.stream(StudentRecordType.values())
                .filter(type -> type.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidRecordTypeException(StudentRecordErrorCode.RECORD_TYPE_NOT_FOUND, value));
    }
}
