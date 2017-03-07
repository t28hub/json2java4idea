package io.t28.json2java.core.builder;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.moshi.Json;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.t28.json2java.core.Assertions.assertThat;

public class MoshiClassBuilderTest {
    private MoshiClassBuilder underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new MoshiClassBuilder(
                DefaultNamePolicy.FIELD,
                DefaultNamePolicy.METHOD,
                DefaultNamePolicy.PARAMETER
        );
    }

    @Test
    public void buildFields() throws Exception {
        // setup
        underTest.addProperty("foo", TypeName.INT)
                .addProperty("bar", TypeName.OBJECT)
                .addProperty("baz", TypeName.BOOLEAN);

        // exercise
        final List<FieldSpec> actual = underTest.buildFields();

        // verify
        assertThat(actual)
                .hasSize(3)
                .doesNotContainNull();

        assertThat(actual.get(0))
                .hasType(TypeName.INT)
                .hasName("foo")
                .isPrivate()
                .isFinal()
                .hasNoInitializer()
                .hasAnnotation(AnnotationSpec.builder(Json.class)
                        .addMember("name", "$S", "foo")
                        .build());

        assertThat(actual.get(1))
                .hasType(TypeName.OBJECT)
                .hasName("bar")
                .isPrivate()
                .isFinal()
                .hasNoInitializer()
                .hasAnnotation(AnnotationSpec.builder(Json.class)
                        .addMember("name", "$S", "bar")
                        .build());

        assertThat(actual.get(2))
                .hasType(TypeName.BOOLEAN)
                .hasName("baz")
                .isPrivate()
                .isFinal()
                .hasNoInitializer()
                .hasAnnotation(AnnotationSpec.builder(Json.class)
                        .addMember("name", "$S", "baz")
                        .build());
    }

    @Test
    public void buildMethods() throws Exception {
        // setup
        underTest.addProperty("foo", TypeName.INT)
                .addProperty("bar", TypeName.OBJECT)
                .addProperty("baz", TypeName.BOOLEAN);

        // exercise
        final List<MethodSpec> actual = underTest.buildMethods();

        // verify
        assertThat(actual)
                .hasSize(4)
                .doesNotContainNull();

        assertThat(actual.get(0))
                .isNotConstructor()
                .hasName("getFoo")
                .hasNoAnnotation()
                .isPublic()
                .hasReturnType(TypeName.INT)
                .hasNoParameter()
                .hasNoException()
                .hasStatement("return foo");

        assertThat(actual.get(1))
                .isNotConstructor()
                .hasName("getBar")
                .hasNoAnnotation()
                .isPublic()
                .hasReturnType(TypeName.OBJECT)
                .hasNoParameter()
                .hasNoException()
                .hasStatement("return bar");

        assertThat(actual.get(2))
                .isNotConstructor()
                .hasName("isBaz")
                .hasNoAnnotation()
                .isPublic()
                .hasReturnType(TypeName.BOOLEAN)
                .hasNoParameter()
                .hasNoException()
                .hasStatement("return baz");

        assertThat(actual.get(3))
                .isConstructor()
                .isPublic()
                .hasNoAnnotation()
                .hasNoException()
                .hasParameter(TypeName.INT, "foo")
                .hasParameter(TypeName.OBJECT, "bar")
                .hasParameter(TypeName.BOOLEAN, "baz")
                .hasCode(CodeBlock.builder()
                        .addStatement("this.$1L = $1L", "foo")
                        .addStatement("this.$1L = $1L", "bar")
                        .addStatement("this.$1L = $1L", "baz")
                        .build());
    }
}