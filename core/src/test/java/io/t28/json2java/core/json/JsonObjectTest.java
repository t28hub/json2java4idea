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

package io.t28.json2java.core.json;

import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.stream.Stream;

import static io.t28.json2java.core.Assertions.assertThat;

public class JsonObjectTest {
    private JsonObject underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JsonObject(ImmutableMap.of("foo", 42, "bar", 32));
    }

    @Test
    public void getType() throws Exception {
        //exercise
        final TypeName actual = underTest.getType();

        // verify
        assertThat(actual)
                .isEqualTo(ParameterizedTypeName.get(Map.class, String.class, Object.class));
    }

    @Test
    public void getValue() throws Exception {
        // exercise
        final Map<String, Object> actual = underTest.getValue();

        // verify
        assertThat(actual)
                .hasSize(2)
                .containsEntry("foo", 42)
                .containsEntry("bar", 32);
    }

    @Test
    public void stream() throws Exception {
        // exercise
        final Stream<Map.Entry<String, JsonValue>> actual = underTest.stream();

        // verify
        assertThat(actual)
                .hasSize(2)
                .doesNotContainNull();
    }
}