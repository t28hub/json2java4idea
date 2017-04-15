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

package io.t28.json2java.core.annotation;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.Before;
import org.junit.Test;

import static io.t28.json2java.core.Assertions.assertThat;

public class SuppressWarningsAnnotationPolicyTest {
    private SuppressWarningsAnnotationPolicy underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new SuppressWarningsAnnotationPolicy("all");
    }

    @Test
    public void applyShouldAddGeneratedAnnotation() throws Exception {
        // setup
        final TypeSpec.Builder builder = TypeSpec.classBuilder("Test");

        // exercise
        underTest.apply(builder);

        // verify
        assertThat(builder.build())
                .hasName("Test")
                .hasAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                        .addMember("value", "$S", "all")
                        .build());
    }
}