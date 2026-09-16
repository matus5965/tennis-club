package cz.inqool.tennisclub;

import cz.inqool.tennisclub.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class TennisClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(TennisClubApplication.class, args);
    }

}
