package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.domain.AuthorizationCode;
import com.edumate.eduserver.auth.domain.AuthorizeStatus;
import com.edumate.eduserver.auth.exception.AlreadyUsedCodeException;
import com.edumate.eduserver.auth.exception.AuthCodeNotFoundException;
import com.edumate.eduserver.auth.exception.ExpiredCodeException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.repository.AuthorizationCodeRepository;
import com.edumate.eduserver.user.domain.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AuthorizationCodeRepository authorizationCodeRepository;

    @Transactional
    public void verifyEmailCode(final Member member, final String inputCode) {
        AuthorizationCode authorizationCode = findByMember(member);
        validateCode(authorizationCode, inputCode);
        authorizationCode.verified();
    }

    private AuthorizationCode findByMember(final Member member) {
        return authorizationCodeRepository.findByMember(member)
                .orElseThrow(() -> new AuthCodeNotFoundException(AuthErrorCode.AUTH_CODE_NOT_FOUND));
    }

    private void validateCode(final AuthorizationCode authorizationCode, final String inputCode) {
        getValidationException(authorizationCode, inputCode)
                .ifPresent(ex -> {
                    authorizationCode.fail();
                    throw ex;
                });
    }

    private Optional<RuntimeException> getValidationException(AuthorizationCode code, String inputCode) {
        if (code.isExpired()) {
            return Optional.of(new ExpiredCodeException(AuthErrorCode.EXPIRED_ACCESS_TOKEN));
        }
        if (!code.getAuthorizationCode().equals(inputCode)) {
            return Optional.of(new IllegalArgumentException(AuthErrorCode.ILLEGAL_URL_ARGUMENT.getMessage()));
        }
        if (code.getStatus() != AuthorizeStatus.PENDING) {
            return Optional.of(new AlreadyUsedCodeException(AuthErrorCode.ALREADY_USED_CODE));
        }
        return Optional.empty();
    }
}
