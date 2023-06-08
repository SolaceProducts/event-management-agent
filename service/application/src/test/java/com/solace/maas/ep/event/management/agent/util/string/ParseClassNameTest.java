package com.solace.maas.ep.event.management.agent.util.string;

import com.solace.maas.ep.event.management.agent.plugin.task.TaskConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

class ParseClassNameTest {

    @Test
    @SneakyThrows
    void testGetClass() {
        Class string = ParseClassName.getClass("java.lang.String");
        assertEquals("java.lang.String", string.getCanonicalName());
        assertThatNoException();
        Class list = ParseClassName.getClass("java.util.List<java.lang.String>");
        assertEquals("java.util.List", list.getCanonicalName());
    }

    @Test
    @SneakyThrows
    void getParameterizedTypes() {
        TaskConfig.class.getCanonicalName();
        Class[] types = ParseClassName.getParameterizedTypes("java.util.List<"+
                TaskConfig.class.getCanonicalName()+"<java.lang.String>>");
        assertEquals(2, types.length);
        assertEquals(TaskConfig.class.getCanonicalName(), types[0].getCanonicalName());
        assertEquals("java.lang.String", types[1].getCanonicalName());

        Class[] types2 = ParseClassName.getParameterizedTypes("java.util.List<java.lang.String>");
        assertEquals(1, types2.length);
        assertEquals("java.lang.String", types2[0].getCanonicalName());

    }
}