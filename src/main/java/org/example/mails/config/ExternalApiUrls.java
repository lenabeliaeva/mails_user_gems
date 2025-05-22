package org.example.mails.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "external.api")
public class ExternalApiUrls {
    private String calendarBaseUrl;
    private String personBaseUrl;
}
