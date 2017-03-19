/*
 * Copyright (c) 2017 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.t28.json2java.core.builder;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.util.List;

import static io.t28.json2java.core.Assertions.assertThat;

public class ModelClassBuilderTest {
    private ModelClassBuilder underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new ModelClassBuilder(
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
                .hasNoJavadoc()
                .hasNoAnnotation()
                .hasModifier(Modifier.PRIVATE, Modifier.FINAL)
                .hasNoInitializer();

        assertThat(actual.get(1))
                .hasType(TypeName.OBJECT)
                .hasName("bar")
                .hasNoJavadoc()
                .hasNoAnnotation()
                .hasModifier(Modifier.PRIVATE, Modifier.FINAL)
                .hasNoInitializer();

        assertThat(actual.get(2))
                .hasType(TypeName.BOOLEAN)
                .hasName("baz")
                .hasNoJavadoc()
                .hasNoAnnotation()
                .hasModifier(Modifier.PRIVATE, Modifier.FINAL)
                .hasNoInitializer();
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
                .isPublic()
                .hasNoJavaDoc()
                .hasNoAnnotation()
                .hasReturnType(TypeName.INT)
                .hasName("getFoo")
                .hasNoException()
                .hasNoParameter()
                .hasStatement("return foo");

        assertThat(actual.get(1))
                .isNotConstructor()
                .isPublic()
                .hasNoJavaDoc()
                .hasNoAnnotation()
                .hasReturnType(TypeName.OBJECT)
                .hasName("getBar")
                .hasNoException()
                .hasNoParameter()
                .hasStatement("return bar");

        assertThat(actual.get(2))
                .isNotConstructor()
                .isPublic()
                .hasNoJavaDoc()
                .hasNoAnnotation()
                .hasReturnType(TypeName.BOOLEAN)
                .hasName("isBaz")
                .hasNoException()
                .hasNoParameter()
                .hasStatement("return baz");

        assertThat(actual.get(3))
                .isConstructor()
                .isPublic()
                .hasNoJavaDoc()
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