package com.solace.maas.ep.event.management.agent.util;

import com.solace.maas.ep.event.management.agent.util.config.idgenerator.IDGeneratorProperties;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

@Data
@Component
@Profile("!TEST")
public class IDGenerator {

    public static final char[] ALPHABET = "1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static final int ID_LENGTH = 11;
    private static final String DETERM_DELIMITER = "-'"; // DO NOT CHANGE THIS DELIMITER
    private static final String NAME_DELIMITER = "-"; // DO NOT CHANGE THIS DELIMITER
    private final IDGeneratorProperties idGeneratorProperties;
    private Random random;

    @Autowired
    public IDGenerator(IDGeneratorProperties idGeneratorProperties) {
        this.idGeneratorProperties = idGeneratorProperties;
    }

    public String generateDeterministicId(String... attributes) {
        if (attributes != null && attributes.length != 0) {
            return DigestUtils.md5DigestAsHex(String.join(DETERM_DELIMITER, attributes)
                    .getBytes(StandardCharsets.UTF_8));
        }
        return "";
    }

    public String generateNameId(String name) {
        if (StringUtils.isNotBlank(name)) {
            return name.toLowerCase().replaceAll("[^\\dA-Za-z ]", "")
                    .trim().replaceAll("\\s+", NAME_DELIMITER);
        }
        return "";
    }

    public String generateRandomUniqueId() {
        final StringBuilder idBuilder = new StringBuilder();
        final byte[] bytes = new byte[ID_LENGTH];
        random.nextBytes(bytes);

        for (int i = 0; i < ID_LENGTH; i++) {
            int alphaIndex = Math.abs(bytes[i] % ALPHABET.length);
            idBuilder.append(ALPHABET[alphaIndex]);
        }

        return idBuilder.toString();
    }

    @PostConstruct
    public void init() {
        if (idGeneratorProperties.getOriginId() == null) {
            // Fallback to default seed which creates a unique seed for each instance
            random = new SecureRandom();
        } else {
            String instanceSeedString = idGeneratorProperties.getOriginId() + (System.nanoTime() & 65_535);
            byte[] instanceSeed = instanceSeedString.getBytes(StandardCharsets.UTF_8);
            random = new SecureRandom(instanceSeed);

        }


    }
}
