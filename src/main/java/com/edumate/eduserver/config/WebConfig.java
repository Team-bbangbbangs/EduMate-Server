package com.edumate.eduserver.config;

import com.edumate.eduserver.common.StudentRecordTypeConverter;
import com.edumate.eduserver.common.MemberUuIdArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberUuIdArgumentResolver memberUuIdArgumentResolver;
    private final StudentRecordTypeConverter studentRecordTypeConverter;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberUuIdArgumentResolver);
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(studentRecordTypeConverter);
    }
}
