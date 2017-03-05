package io.t28.json2java.core.json;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.Before;
import org.junit.Test;

import static io.t28.json2java.core.Assertions.assertThat;

public class JsonStringTest {
    private JsonString underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JsonString("foo");
    }

    @Test
    public void getType() throws Exception {
        // exercise
        final TypeName actual = underTest.getType();

        // verify
        assertThat(actual)
                .isEqualTo(ClassName.get(String.class));
    }

    @Test
    public void getValue() throws Exception {
        // exercise
        final String actual = underTest.getValue();

        // verify
        assertThat(actual)
                .isEqualTo("foo");
    }
}