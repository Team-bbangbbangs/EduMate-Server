package com.edumate.eduserver.common;

import com.edumate.eduserver.common.annotation.MemberUuid;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberUuidArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        boolean hasMemberUuIdAnnotation = parameter.hasParameterAnnotation(MemberUuid.class);
        boolean isStringType = parameter.getParameterType().equals(String.class);
        return hasMemberUuIdAnnotation && isStringType;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
