package net.codecraft.jejutrip.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class LocalIpEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        try {
            log.info("********************************************************");
            String localIp = InetAddress.getLocalHost().getHostAddress();
            log.info("localIp=" + localIp);
            environment.getSystemProperties().put("local.ip", localIp);
            log.info("********************************************************");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}