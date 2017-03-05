package io.t28.json2java.core.json;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.t28.json2java.core.Assertions.assertThat;

public class JsonArrayTest {
    private JsonArray underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JsonArray(Arrays.asList("foo", "bar", "baz", "qux"));
    }

    @Test
    public void getType() throws Exception {
        // exercise
        final TypeName actual = underTest.getType();

        // verify
        assertThat(actual)
                .isEqualTo(ParameterizedTypeName.get(List.class, Object.class));
    }

    @Test
    public void getValues() throws Exception {
        // exercise
        final List<Object> actual = underTest.getValue();

        // verify
        assertThat(actual)
                .hasSize(4)
                .containsOnlyOnce("foo", "bar", "baz", "qux");
    }

    @Test
    public void stream() throws Exception {
        // exercise
        final Stream<JsonValue> actual = underTest.stream();

        // verify
        assertThat(actual)
                .hasSize(4)
                .doesNotContainNull();
    }
}