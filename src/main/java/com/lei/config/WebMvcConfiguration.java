package com.lei.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${upload.file.view.prefix.url}")
    private String VIEW_PREFIX_URL;

    @Value("${upload.file.win.dir}")
    private String WIN_DIR;

    @Value("${upload.file.linux.dir}")
    private String LINUX_DIR;

    @Value("${upload.file.allow.type}")
    private String FILE_ALLOW_TYPE;

    @Bean(value = "dirPrefix")
    public String dirPrefix() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("win")) {
            return WIN_DIR;
        }
        else {
            return LINUX_DIR;
        }
    }

    @Bean(value = "fileAllowTypes")
    public List<String> fileAllowTypes() {
        if (StringUtils.isBlank(FILE_ALLOW_TYPE)) {
            return Arrays.asList("image/gif", "image/jpeg", "image/png");
        }
        return Arrays.asList(FILE_ALLOW_TYPE.split(","));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("win")) {
            registry.addResourceHandler(VIEW_PREFIX_URL)
                    .addResourceLocations("file:" + WIN_DIR);
        }
        else {
            registry.addResourceHandler(VIEW_PREFIX_URL)
                    .addResourceLocations("file:" + LINUX_DIR);
        }
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
