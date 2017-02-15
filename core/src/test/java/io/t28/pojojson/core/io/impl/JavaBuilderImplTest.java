package io.t28.pojojson.core.io.impl;

import com.squareup.javapoet.TypeSpec;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Modifier;

import static io.t28.pojojson.core.Assertions.assertThat;

public class JavaBuilderImplTest {
    private JavaBuilderImpl underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JavaBuilderImpl();
    }

    @Test
    public void buildShouldReturnJavaCode() throws Exception {
        final TypeSpec typeSpec = TypeSpec.classBuilder("Test")
                .addModifiers(Modifier.PUBLIC)
                .build();

        // exercise
        final String actual = underTest.build("io.t28.example", typeSpec);

        // verify
        assertThat(actual)
                .isEqualTo("package io.t28.example;\n\npublic class Test {\n}\n");
    }
}