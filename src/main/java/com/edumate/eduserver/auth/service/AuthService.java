package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.domain.AuthorizationCode;
import com.edumate.eduserver.auth.domain.AuthorizeStatus;
import com.edumate.eduserver.auth.exception.AuthCodeNotFoundException;
import com.edumate.eduserver.auth.exception.ExpiredCodeException;
import com.edumate.eduserver.auth.exception.MemberAlreadyRegisteredException;
import com.edumate.eduserver.auth.exception.MisMatchedCodeException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.repository.AuthorizationCodeRepository;
import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final AuthorizationCodeRepository authorizationCodeRepository;
    private final MemberRepository memberRepository;

    private static final String EMPTY_REFRESH_TOKEN = null;

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

    @Transactional
    public void logout(final Member member) {
        member.updateRefreshToken(EMPTY_REFRESH_TOKEN);
    }

    public void checkAlreadyRegistered(final String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberAlreadyRegisteredException(AuthErrorCode.MEMBER_ALREADY_REGISTERED);
        }
    }

    private AuthorizationCode getValidCodeByMember(final long memberId) {
        return authorizationCodeRepository.findTop1ByMemberIdAndStatusOrderByIdDesc(memberId, AuthorizeStatus.PENDING)
                .orElseThrow(() -> new AuthCodeNotFoundException(AuthErrorCode.AUTH_CODE_NOT_FOUND));
    }

    private void checkVerified(final AuthorizationCode code, final String inputCode) {
        try {
            validateCode(code, inputCode);
        } catch (ExpiredCodeException | MisMatchedCodeException e) {
            code.fail();
            throw e;
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
