package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.domain.AuthorizationCode;
import com.edumate.eduserver.auth.domain.AuthorizeStatus;
import com.edumate.eduserver.auth.repository.AuthorizationCodeRepository;
import com.edumate.eduserver.user.domain.Member;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RandomCodeGenerator {

    private final AuthorizationCodeRepository authorizationCodeRepository;

    private static final int CODE_LENGTH = 6;
    private static final int MAX_VALUE = 10;

    public String getCode(final Member member) {
        String authCode = generate();
        saveCode(authCode, member);
        return authCode;
    }

    private void saveCode(final String code, final Member member) {
        AuthorizationCode authorizationCode = AuthorizationCode.create(member, code, AuthorizeStatus.PENDING);
        authorizationCodeRepository.save(authorizationCode);
    }

    private String generate() {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            builder.append(random.nextInt(MAX_VALUE));
        }
        return builder.toString();
    }
}
