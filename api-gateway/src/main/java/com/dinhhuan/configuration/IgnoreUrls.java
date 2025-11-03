package com.dinhhuan.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "secure.ignored")
public class IgnoreUrls {
    private List<String> urls = new ArrayList<>();
    public List<String> getUrls() {
        return urls;
    }
}
