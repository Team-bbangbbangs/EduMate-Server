package com.edumate.eduserver.external.ai.service;

import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    private static final String PROMPT_TEMPLATE = """
           다음 지침에 따라 '%s' 항목의 생활기록부를 작성해주세요.
           
           ## 작성 기준
           - 목표 분량: %d바이트 (최소 %d바이트 이상)
             - 분량이 부족할 경우 구체적인 예시와 상세한 설명을 추가하여 목표 분량에 맞춰 작성해주세요.
           - 문체: 현재형 음슴체 ('~임', '~함' 형태)
           - 구두점: 쉼표 사용 최소화
           - '학생은', '학생이' 등의 표현은 사용하지 않음
           
           ## 바이트 수 계산법
           - 한글: 1글자당 3바이트
           - 영문/숫자: 1글자당 1바이트
           - 줄바꿈: 1바이트

           ## 작성 내용
           다음 정보를 바탕으로 구체적이고 상세한 생활기록부를 작성하세요: %s
          
           ## 중요: 작성 방식
           아래 모든 정보를 각각의 버전에서 완전히 다른 방식으로 표현하세요:
           - 입력으로 들어온 전체 내용에 대해 각각 3가지의 서술 버전을 작성
           - 반드시 전체의 입력값을 토대로 각 버전을 생성함. 일부분만 발췌해서 작성하지 않음
           - 같은 활동이라도 다른 관점에서 서술
           - 다른 강조점과 표현 방식 사용
           - 각 버전은 전체 내용을 포함하되 구성과 초점을 달리함
           
           ## 중요: 응답 형식
           반드시 정확한 JSON 형식으로만 응답하세요. 추가 텍스트나 설명은 포함하지 마세요:
           주의: JSON 형식을 정확히 지켜주세요. description1, description2, description3을 키로 사용합니다. 큰따옴표와 중괄호를 정확히 사용하세요.
            """;

    public String createPrompt(final StudentRecordType recordType, final String inputPrompt) {
        int byteCount = recordType.getByteCount();
        return String.format(PROMPT_TEMPLATE,
                recordType.name(),
                byteCount,
                byteCount - 20,
                inputPrompt
        );
    }
}
