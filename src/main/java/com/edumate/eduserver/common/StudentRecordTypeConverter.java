package com.edumate.eduserver.common;

import com.edumate.eduserver.common.code.BusinessErrorCode;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.RecordTypeNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StudentRecordTypeConverter implements Converter<String, StudentRecordType> {

    @Override
    public StudentRecordType convert(final String source) {
        try {
            return StudentRecordType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RecordTypeNotFoundException(BusinessErrorCode.BAD_REQUEST);
        }
    }
}
