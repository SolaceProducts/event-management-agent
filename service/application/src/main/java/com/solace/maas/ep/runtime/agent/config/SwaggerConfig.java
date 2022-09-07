package com.solace.maas.ep.runtime.agent.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swagger(ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:swagger-description.md");
        String description;
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            description = FileCopyUtils.copyToString(reader);
        }

        return new OpenAPI()
                .info(new Info()
                        .title("Runtime Agent Open API")
                        .version("1.0.0")
                        .description(description))
                .tags(getAllTags());
    }

    private List<Tag> getAllTags() {
        return List.of(
                createTagFromName(SwaggerControllerTags.MESSAGING_SERVICES_TAG)
        );
    }

    private Tag createTagFromName(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }
}
