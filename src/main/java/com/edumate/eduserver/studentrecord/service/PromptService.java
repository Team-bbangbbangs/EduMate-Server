package com.edumate.eduserver.studentrecord.service;

import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    private static final String PROMPT_TEMPLATE = """
            생활기록부 유형 %s에 대한 생활기록부 내용을 작성해주세요.
            
            이때, 다음 요구사항을 정확히 지켜주세요:
                1. 글자수는 UTF-8 기준으로 정확히 %d 바이트에 최대한 가깝게 작성
                2. %d 바이트 미만이지만 최소 %d 바이트 이상으로 반드시 작성
                3. 바이트 수가 부족하면 구체적인 예시, 상세한 설명, 추가 정보를 포함해서 길이를 늘려주세요
                4. 입력 글자의 단위는 Byte이며 한글 1글자는 3바이트, 영문/숫자는 1바이트, 엔터는 1바이트 로 계산
                5. 목표: %d 바이트 근처의 충분한 분량으로 작성
                **중요: 짧게 끝내지 말고 반드시 %d 바이트에 가까운 긴 글로 작성해주세요**
                6. 문장은 '~임' '~함'과 같이 현재형 음슴체로 작성
                7. 쉽표 사용은 최대한 자제
                8. 입력된 내용 %s를 바탕으로 작성
            """;

    public String createPrompt(final String subject, final StudentRecordType recordType, final String inputPrompt) {
        int byteCount = recordType.getByteCount();
        return String.format(PROMPT_TEMPLATE, recordType.name(), byteCount, byteCount, byteCount - 20, byteCount,
                byteCount, inputPrompt);
    }
}
