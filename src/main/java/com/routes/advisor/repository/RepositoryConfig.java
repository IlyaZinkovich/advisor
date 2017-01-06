package com.routes.advisor.repository;

import com.routes.advisor.model.Route;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Configuration
@EntityScan(basePackageClasses = { Route.class })
public class RepositoryConfig {
}
