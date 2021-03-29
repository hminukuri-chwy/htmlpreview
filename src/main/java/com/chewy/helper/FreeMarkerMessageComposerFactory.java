package com.chewy.helper;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Version;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Introspected;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Factory
@Introspected
public class FreeMarkerMessageComposerFactory {
    private final String freemarkerTemplatePath;

    public FreeMarkerMessageComposerFactory(@NonNull @Value("${email.local.template.dir}") String freemarkerTemplatePath) {
        this.freemarkerTemplatePath = freemarkerTemplatePath;
    }

    @Bean
    public Configuration freemarkerConfiguration() throws IOException {
        Version version = freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;
        freemarker.template.Configuration configuration =
                new freemarker.template.Configuration(version);
        configuration.setDirectoryForTemplateLoading(new File(freemarkerTemplatePath));
        configuration.setObjectWrapper(new DefaultObjectWrapper(version));
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return  configuration;
    }
}


