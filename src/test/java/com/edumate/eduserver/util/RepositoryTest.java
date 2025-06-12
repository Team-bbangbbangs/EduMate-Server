package com.edumate.eduserver.util;

import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@DataJpaTest(excludeAutoConfiguration = {FlywayAutoConfiguration.class})
public class RepositoryTest {
}
