package com.edumate.eduserver.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.RecordTypeNotFoundException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("학생 기록 타입 변환기 테스트")
class StudentRecordTypeConverterTest {

    private final StudentRecordTypeConverter converter = new StudentRecordTypeConverter();

    @Test
    @DisplayName("정상적인 값은 StudentRecordType으로 변환된다")
    void convert_validValue() {
        assertThat(converter.convert("ability")).isEqualTo(StudentRecordType.ABILITY_DETAIL);
        assertThat(converter.convert("behavior")).isEqualTo(StudentRecordType.BEHAVIOR_OPINION);
    }

    @Test
    @DisplayName("존재하지 않는 값은 RecordTypeNotFoundException을 발생시킨다")
    void convert_invalidValue() {
        assertThatThrownBy(() -> converter.convert("invalid"))
                .isInstanceOf(RecordTypeNotFoundException.class)
                .hasMessageContaining(StudentRecordErrorCode.RECORD_TYPE_NOT_FOUND.getMessage());
    }
}
