package io.t28.json2java.core.io;

import com.squareup.javapoet.TypeSpec;
import io.t28.json2java.core.io.JavaBuilderImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Modifier;

import static io.t28.json2java.core.Assertions.assertThat;

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
        Assertions.assertThat(actual)
                .isEqualTo("package io.t28.example;\n\npublic class Test {\n}\n");
    }
}