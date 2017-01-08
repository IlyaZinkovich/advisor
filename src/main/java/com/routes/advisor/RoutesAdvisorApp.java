package com.routes.advisor;

import com.routes.advisor.client.ClientConfig;
import com.routes.advisor.web.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {ClientConfig.class, WebConfig.class})
public class RoutesAdvisorApp {

    public static void main(String[] args) {
        SpringApplication.run(RoutesAdvisorApp.class);
    }
}
