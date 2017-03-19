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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.t28.json2java.core.Assertions.assertThat;

public class JacksonClassBuilderTest {
    private JacksonClassBuilder underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JacksonClassBuilder(
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
                .hasNoAnnotation();

        assertThat(actual.get(1))
                .hasType(TypeName.OBJECT)
                .hasName("bar")
                .isPrivate()
                .isFinal()
                .hasNoInitializer()
                .hasNoAnnotation();

        assertThat(actual.get(2))
                .hasType(TypeName.BOOLEAN)
                .hasName("baz")
                .isPrivate()
                .isFinal()
                .hasNoInitializer()
                .hasNoAnnotation();
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
                .hasAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", "foo")
                        .build())
                .isPublic()
                .hasReturnType(TypeName.INT)
                .hasNoParameter()
                .hasNoException()
                .hasStatement("return foo");

        assertThat(actual.get(1))
                .isNotConstructor()
                .hasName("getBar")
                .hasAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", "bar")
                        .build())
                .isPublic()
                .hasReturnType(TypeName.OBJECT)
                .hasNoParameter()
                .hasNoException()
                .hasStatement("return bar");

        assertThat(actual.get(2))
                .isNotConstructor()
                .hasName("isBaz")
                .hasAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", "baz")
                        .build())
                .isPublic()
                .hasReturnType(TypeName.BOOLEAN)
                .hasNoParameter()
                .hasNoException()
                .hasStatement("return baz");
    }
}