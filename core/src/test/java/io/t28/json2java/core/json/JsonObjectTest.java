package io.t28.json2java.core.json;

import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.stream.Stream;

import static io.t28.json2java.core.Assertions.assertThat;

public class JsonObjectTest {
    private JsonObject underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JsonObject(ImmutableMap.of("foo", 42, "bar", 32));
    }

    @Test
    public void getType() throws Exception {
        //exercise
        final TypeName actual = underTest.getType();

        // verify
        assertThat(actual)
                .isEqualTo(ParameterizedTypeName.get(Map.class, String.class, Object.class));
    }

    @Test
    public void getValue() throws Exception {
        // exercise
        final Map<String, Object> actual = underTest.getValue();

        // verify
        assertThat(actual)
                .hasSize(2)
                .containsEntry("foo", 42)
                .containsEntry("bar", 32);
    }

    @Test
    public void stream() throws Exception {
        // exercise
        final Stream<Map.Entry<String, JsonValue>> actual = underTest.stream();

        // verify
        assertThat(actual)
                .hasSize(2)
                .doesNotContainNull();
    }
}