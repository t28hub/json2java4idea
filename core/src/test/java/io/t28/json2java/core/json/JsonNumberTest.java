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

import com.squareup.javapoet.TypeName;
import org.junit.Before;
import org.junit.Test;

import static io.t28.json2java.core.Assertions.assertThat;

public class JsonNumberTest {
    private JsonNumber underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new JsonNumber(int.class, 42);
    }

    @Test
    public void getType() throws Exception {
        // exercise
        final TypeName actual = underTest.getType();

        // verify
        assertThat(actual)
                .isEqualTo(TypeName.INT);
    }

    @Test
    public void getValue() throws Exception {
        // exercise
        final Number actual = underTest.getValue();

        // verify
        assertThat(actual)
                .isInstanceOf(Integer.class)
                .isEqualTo(42);
    }
}