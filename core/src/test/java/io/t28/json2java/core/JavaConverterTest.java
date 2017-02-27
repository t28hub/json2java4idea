package io.t28.json2java.core;

import com.google.common.io.Files;
import io.t28.json2java.core.io.JsonParserImpl;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.io.JavaBuilderImpl;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class JavaConverterTest {
    private static final String JSON_RESOURCE_DIR = "src/test/resources/json";
    private static final String JAVA_RESOURCE_DIR = "src/test/resources/java";
    private static final String PACKAGE_NAME = "io.t28.test";

    private JavaConverter underTest;

    @Before
    public void setUp() throws Exception {
        final Configuration configuration = Configuration.builder()
                .style(Style.NONE)
                .classNamePolicy(DefaultNamePolicy.CLASS)
                .methodNamePolicy(DefaultNamePolicy.METHOD)
                .fieldNamePolicy(DefaultNamePolicy.FIELD)
                .parameterNamePolicy(DefaultNamePolicy.PARAMETER)
                .jsonParser(new JsonParserImpl())
                .javaBuilder(new JavaBuilderImpl())
                .build();
        underTest = new JavaConverter(configuration);
    }

    @Test
    public void convertShouldThrowExceptionWhenJsonIsNull() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> underTest.convert(PACKAGE_NAME, "NullTest", "null"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value must be an Object or Array");
    }

    @Test
    public void convertShouldThrowExceptionWhenJsonIsBoolean() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> underTest.convert(PACKAGE_NAME, "BooleanTest", "true"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value must be an Object or Array");
    }

    @Test
    public void convertShouldThrowExceptionWhenJsonIsNumber() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> underTest.convert(PACKAGE_NAME, "NumberTest", "1024"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value must be an Object or Array");
    }

    @Test
    public void convertShouldThrowExceptionWhenJsonIsString() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> underTest.convert(PACKAGE_NAME, "StringTest", "\"\""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("value must be an Object or Array");
    }

    @Test
    public void convertShouldThrowExceptionWhenJsonIsEmptyArray() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> underTest.convert(PACKAGE_NAME, "EmptyArray", "[]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot create class from empty array or primitive array");
    }


    @Test
    public void convertShouldThrowExceptionWhenJsonIsNestedEmptyArray() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> underTest.convert(PACKAGE_NAME, "NestedEmptyArray", "[[]]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot create class from empty array or primitive array");
    }

    @Test
    public void convertShouldCreateClassWhenJsonIsComplexArray() throws Exception {
        // setup
        final String json = readJson("complex_array.json");

        // exercise
        final String actual = underTest.convert(PACKAGE_NAME, "ComplexArrayTest", json);

        // verify
        final String java = readJava("ComplexArrayTest.java");
        assertThat(actual)
                .isEqualTo(java);
    }

    @Test
    public void convertShouldCreateClassWhenJsonIsArray() throws Exception {
        // setup
        final String json = readJson("array.json");

        // exercise
        final String actual = underTest.convert(PACKAGE_NAME, "ArrayTest", json);

        // verify
        final String java = readJava("ArrayTest.java");
        assertThat(actual)
                .isEqualTo(java);
    }

    @Test
    public void convertShouldCreateClassWhenJsonIsEmptyObject() throws Exception {
        // exercise
        final String actual = underTest.convert(PACKAGE_NAME, "EmptyObjectTest", "{}");

        // verify
        final String java = readJava("EmptyObjectTest.java");
        assertThat(actual)
                .isEqualTo(java);
    }

    @Test
    public void convertShouldCreateClassWhenJsonIsObject() throws Exception {
        // setup
        final String json = readJson("object.json");

        // exercise
        final String actual = underTest.convert(PACKAGE_NAME, "ObjectTest", json);

        // verify
        final String java = readJava("ObjectTest.java");
        assertThat(actual)
                .isEqualTo(java);
    }

    @Nonnull
    private static String readJson(@Nonnull String name) throws Exception {
        final File file = new File(JSON_RESOURCE_DIR, name);
        return Files.toString(file, StandardCharsets.UTF_8);
    }

    @Nonnull
    private static String readJava(@Nonnull String name) throws Exception {
        final File file = new File(JAVA_RESOURCE_DIR, name);
        return Files.toString(file, StandardCharsets.UTF_8);
    }
}