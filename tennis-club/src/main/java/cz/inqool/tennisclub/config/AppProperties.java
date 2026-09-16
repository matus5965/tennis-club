package cz.inqool.tennisclub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(boolean dataInitEnabled) {
}
