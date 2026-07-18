package com.solace.maas.ep.event.management.agent;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockitoAnnotations;

/**
 * SB4 replacement for the deleted Spring Boot {@code MockitoTestExecutionListener} (DATAGO-142260).
 *
 * <p>Under Spring Boot 3.x that listener auto-initialized {@code @Mock}/{@code @Captor}/
 * {@code @InjectMocks} fields inside {@code @SpringBootTest} classes. Spring Boot 4 removed it, so
 * those fields stay {@code null} and the tests NPE. EMA has ~45 such test classes and none call
 * {@code MockitoAnnotations.openMocks(this)} themselves.
 *
 * <p>This extension restores that behaviour globally (registered via
 * {@code META-INF/services/org.junit.jupiter.api.extension.Extension} +
 * {@code junit.jupiter.extensions.autodetection.enabled=true}). It is a no-op for test classes
 * with no Mockito-annotated fields, and it skips classes that drive Mockito themselves via
 * {@code @ExtendWith(MockitoExtension.class)} to avoid double-initialization.
 */
public class MockitoFieldInitExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        ExtendWith extendWith = testClass.getAnnotation(ExtendWith.class);
        if (extendWith != null) {
            for (Class<?> extension : extendWith.value()) {
                if (extension.getName().contains("MockitoExtension")) {
                    return;
                }
            }
        }
        MockitoAnnotations.openMocks(context.getRequiredTestInstance());
    }
}
