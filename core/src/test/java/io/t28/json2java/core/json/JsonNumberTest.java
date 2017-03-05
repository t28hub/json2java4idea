package io.t28.json2java.core.json;

import com.squareup.javapoet.TypeName;
import org.junit.Before;
import org.junit.Test;

import static io.t28.json2java.core.Assertions.assertThat;

public class JsonNumberTest {
    private JsonNumber underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JsonNumber(int.class, 42);
    }

    @Test
    public void getType() throws Exception {
        // exercise
        final TypeName actual = underTest.getType();

        // verify
        assertThat(actual)
                .isEqualTo(TypeName.INT);
    }

    @Test
    public void getValue() throws Exception {
        // exercise
        final Number actual = underTest.getValue();

        // verify
        assertThat(actual)
                .isInstanceOf(Integer.class)
                .isEqualTo(42);
    }
}