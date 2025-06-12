package com.edumate.eduserver.common;

import com.edumate.eduserver.common.annotation.UserId;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MemberUuIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        boolean hasUserIdAnnotation = parameter.hasParameterAnnotation(UserId.class);
        boolean isLongType = parameter.getParameterType().equals(Long.class) || parameter.getParameterType().equals(long.class);
        return hasUserIdAnnotation && isLongType;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
