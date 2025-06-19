package com.edumate.eduserver.studentrecord.service;

import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.InvalidRecordTypeException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    public String createPrompt(final String subject, final StudentRecordType recordType, final String inputPrompt) {

        return String.format("생활기록부 유형 %s에 대한 생활기록부를 작성하려고 해 " +
                        "글자수는 UTF-8기준 %d 바이트 미만으로 작성해줘. 대신 최대한 %d 바이트 수에 가깝게 작성해줘" +
                        "입력된 내용은 다음과 같습니다: %s" +
                        "이 정보를 바탕으로 총 3가지 버전의 생활기록부를 작성해서 JSON 형태로 제공해주세요." +
                        "각 버전은 description1, description2, description3 키를 포함해야 합니다.",
                recordType.name(), getByteCount(recordType), getByteCount(recordType), inputPrompt);
    }

    private int getByteCount(final StudentRecordType recordType) {
        if (recordType == StudentRecordType.ABILITY_DETAIL) {
            return 1000;
        } else if (recordType == StudentRecordType.BEHAVIOR_OPINION) {
            return 800;
        } else if (recordType == StudentRecordType.CREATIVE_ACTIVITY_CAREER) {
            return 1200;
        } else if (recordType == StudentRecordType.CREATIVE_ACTIVITY_AUTONOMY) {
            return 900;
        } else if (recordType == StudentRecordType.CREATIVE_ACTIVITY_CLUB) {
            return 1100;
        }
        throw new InvalidRecordTypeException(StudentRecordErrorCode.RECORD_TYPE_NOT_FOUND);
    }
}
