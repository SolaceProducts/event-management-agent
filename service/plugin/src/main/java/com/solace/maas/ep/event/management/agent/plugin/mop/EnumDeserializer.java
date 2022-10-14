package com.solace.maas.ep.event.management.agent.plugin.mop;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by tzoght on 2017-07-10.
 * A generic enum deserializer for Jackson to convert unknown enums into null instead of throwing an exception
 * @param <T> The enum being deserialized
 */
@Slf4j
public final class EnumDeserializer<T extends Enum> extends JsonDeserializer<T> {
    Class<T> clazz;

    public EnumDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        try {
            return (T) clazz.getMethod("valueOf", String.class).invoke(String.class, parser.getValueAsString());
        } catch (Exception e) {
            log.error("Error deserializing an enum", e);
        }

        return null;
    }
}
