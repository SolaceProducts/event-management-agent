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

        assertThat(id).hasSize(11);
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

    /**
     * Test the uniqueness of the generated random unique id with originId set to null.
     * The test is run multiple times to ensure the uniqueness of the generated id.
     */
    @SneakyThrows
    @Test
    public void testUniquenessOfGeneratedRandomUniqueIdOfDistinctIDGeneratorWithoutOriginId() {
        IDGeneratorProperties props = new IDGeneratorProperties();
        props.setOriginId(null);
        IDGenerator idGenerator = new IDGenerator(props);
        idGenerator.init();
        String id1 = idGenerator.generateRandomUniqueId();
        String id2 = idGenerator.generateRandomUniqueId();
        assertThat(id1).isNotEqualTo(id2);
    }

    /**
     * Test the uniqueness of the generated random unique id with originId set to a specific value.
     * The test is run multiple times to ensure the uniqueness of the generated id.
     */
    @SneakyThrows
    @Test
    public void testUniquenessOfGeneratedRandomUniqueIdOfDistinctIDGeneratorWithOriginId() {
        IDGeneratorProperties props = new IDGeneratorProperties();
        props.setOriginId("abc123");
        IDGenerator idGenerator = new IDGenerator(props);
        idGenerator.init();
        String id1 = idGenerator.generateRandomUniqueId();
        String id2 = idGenerator.generateRandomUniqueId();
        assertThat(id1).isNotEqualTo(id2);
    }

    /**
     * Test the uniqueness of the generated random unique id with originId set to null.
     * The test IDGenerator is created twice to ensure uniqueness between different instances.
     */
    @SneakyThrows
    @Test
    public void testUniquenessOfGeneratedRandomUniqueIdOfNewIdGeneratorsWithoutOriginId() {
        IDGeneratorProperties props = new IDGeneratorProperties();
        props.setOriginId(null);

        IDGenerator idGenerator = new IDGenerator(props);
        idGenerator.init();
        String id1 = idGenerator.generateRandomUniqueId();

        idGenerator = new IDGenerator(props);
        idGenerator.init();
        String id2 = idGenerator.generateRandomUniqueId();
        assertThat(id1).isNotEqualTo(id2);
    }

    /**
     * Test the uniqueness of the generated random unique id with originId set to a specific value.
     * The test IDGenerator is created twice to ensure uniqueness between different instances.
     */
    @SneakyThrows
    @Test
    public void testUniquenessOfGeneratedRandomUniqueIdOfNewIdGeneratorsWithOriginId() {
        IDGeneratorProperties props = new IDGeneratorProperties();
        props.setOriginId("abc123");

        IDGenerator idGenerator = new IDGenerator(props);
        idGenerator.init();
        String id1 = idGenerator.generateRandomUniqueId();

        idGenerator = new IDGenerator(props);
        idGenerator.init();
        String id2 = idGenerator.generateRandomUniqueId();
        assertThat(id1).isNotEqualTo(id2);
    }
}
