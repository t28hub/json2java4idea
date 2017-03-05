package io.t28.json2java.core.json;

import com.squareup.javapoet.TypeName;
import org.junit.Before;
import org.junit.Test;

import static io.t28.json2java.core.Assertions.assertThat;

public class JsonNullTest {
    private JsonNull underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JsonNull();
    }

    @Test
    public void getType() throws Exception {
        // exercise
        final TypeName actual = underTest.getType();

        // verify
        assertThat(actual)
                .isEqualTo(TypeName.OBJECT);
    }

    @Test
    public void getValue() throws Exception {
        // exercise
        final Object actual = underTest.getValue();

        // verify
        assertThat(actual)
                .isNull();
    }
}