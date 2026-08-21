package com.solace.maas.ep.event.management.agent;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockitoAnnotations;

/**
 * SB4 replacement for the deleted Spring Boot {@code MockitoTestExecutionListener} (DATAGO-142260):
 * under SB4, {@code @Mock} fields in {@code @SpringBootTest} classes are no longer auto-initialized
 * and stay {@code null} (~45 such classes here, none calling {@code openMocks} themselves). Registered
 * globally via {@code META-INF/services} + extension autodetection; skips classes that drive Mockito
 * themselves via {@code @ExtendWith(MockitoExtension.class)}.
 */
public class MockitoFieldInitExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        // @ExtendWith is @Repeatable — getAnnotationsByType covers single + repeated declarations.
        for (ExtendWith extendWith : testClass.getAnnotationsByType(ExtendWith.class)) {
            for (Class<?> extension : extendWith.value()) {
                if (extension.getName().contains("MockitoExtension")) {
                    return;
                }
            }
        }
        MockitoAnnotations.openMocks(context.getRequiredTestInstance());
    }
}
