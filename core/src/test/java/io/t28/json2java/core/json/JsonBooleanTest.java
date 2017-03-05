package io.t28.json2java.core.json;

import com.squareup.javapoet.TypeName;
import org.junit.Before;
import org.junit.Test;

import static io.t28.json2java.core.Assertions.assertThat;

public class JsonBooleanTest {
    private JsonBoolean underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JsonBoolean(true);
    }

    @Test
    public void getType() throws Exception {
        // exercise
        final TypeName actual = underTest.getType();

        // verify
        assertThat(actual)
                .isEqualTo(TypeName.BOOLEAN);
    }

    @Test
    public void getValue() throws Exception {
        // exercise
        final Boolean actual = underTest.getValue();

        // verify
        assertThat(actual)
                .isNotNull()
                .isTrue();
    }
}