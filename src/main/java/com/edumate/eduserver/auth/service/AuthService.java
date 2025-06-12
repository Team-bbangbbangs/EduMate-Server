package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.domain.AuthorizationCode;
import com.edumate.eduserver.auth.domain.AuthorizeStatus;
import com.edumate.eduserver.auth.exception.AuthCodeNotFoundException;
import com.edumate.eduserver.auth.exception.ExpiredCodeException;
import com.edumate.eduserver.auth.exception.MisMatchedCodeException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.repository.AuthorizationCodeRepository;
import com.edumate.eduserver.user.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AuthorizationCodeRepository authorizationCodeRepository;

    @Transactional
    public void verifyEmailCode(final Member member, final String inputCode) {
        AuthorizationCode authorizationCode = getValidCodeByMember(member.getId());
        checkVerified(authorizationCode, inputCode);
        authorizationCode.verified();
    }

    @Transactional
    public void saveCode(final Member member, final String code) {
        AuthorizationCode authorizationCode = AuthorizationCode.create(member, code, AuthorizeStatus.PENDING);
        authorizationCodeRepository.save(authorizationCode);
    }

    private AuthorizationCode getValidCodeByMember(final long memberId) {
        return authorizationCodeRepository.findTop1ByMemberIdAndStatusOrderByIdDesc(memberId, AuthorizeStatus.PENDING)
                .orElseThrow(() -> new AuthCodeNotFoundException(AuthErrorCode.AUTH_CODE_NOT_FOUND));
    }

    private void checkVerified(final AuthorizationCode code, final String inputCode) {
        try {
            validateCode(code, inputCode);
        } catch (ExpiredCodeException | MisMatchedCodeException e) {
            log.error("Code validation failed: {}", e.getMessage());
            throw e;
        } finally {
            code.fail();
        }
    }

    private void validateCode(final AuthorizationCode code, final String inputCode) {
        if (code.isExpired()) {
            throw new ExpiredCodeException(AuthErrorCode.EXPIRED_CODE);
        }
        if (!code.getAuthorizationCode().equals(inputCode)) {
            throw new MisMatchedCodeException(AuthErrorCode.INVALID_CODE);
        }
    }
}
