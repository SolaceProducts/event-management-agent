package com.solace.maas.ep.event.management.agent.util;

import com.solace.maas.ep.event.management.agent.TestConfig;
import com.solace.maas.ep.event.management.agent.util.config.idgenerator.IDGeneratorProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
public class IDGeneratorTests {

    @Autowired
    Random random;

    @Mock
    IDGeneratorProperties idGeneratorProperties;

    @SneakyThrows
    @Test
    public void testGenerateRandomUniqueId() {
        IDGenerator idGenerator = new IDGenerator(idGeneratorProperties);
        idGenerator.setRandom(random);

        String id = idGenerator.generateRandomUniqueId();

        assertThat(id).hasSize(12);
    }

    @SneakyThrows
    @Test
    public void testGenerateNameId() {
        IDGenerator idGenerator = new IDGenerator(idGeneratorProperties);
        String nameId = idGenerator.generateNameId("test_With@Special#Chars");

        assertThat(nameId).isEqualTo("testwithspecialchars");
        assertThat(nameId).hasSize("testwithspecialchars".length());

        String emptyNameId = idGenerator.generateNameId("");
        assertThat(emptyNameId).isEmpty();
    }

    @SneakyThrows
    @Test
    public void testGenerateDeterministicId() {
        IDGenerator idGenerator = new IDGenerator(idGeneratorProperties);
        String nameId = idGenerator.generateDeterministicId("test_With@Special#Chars", "testWithoutSpecialChar");

        assertThat(nameId).hasSize(32);

        String newNameId = idGenerator.generateDeterministicId(null);
        assertThat(newNameId).isEmpty();
    }

    @SneakyThrows
    @Test
    public void testInit() {
        IDGenerator idGenerator = new IDGenerator(idGeneratorProperties);
        idGenerator.init();

        assertThatNoException();
    }
}
