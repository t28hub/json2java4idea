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

package io.t28.json2java.core.io;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.t28.json2java.core.io.JavaBuilder;
import io.t28.json2java.core.io.exception.JavaBuildException;

import javax.annotation.Nonnull;

public class JavaBuilderImpl implements JavaBuilder {
    private static final String INDENT = "    ";

    @Nonnull
    @Override
    public String build(@Nonnull String packageName, @Nonnull TypeSpec typeSpec) throws JavaBuildException {
        return JavaFile.builder(packageName, typeSpec)
                .indent(INDENT)
                .skipJavaLangImports(true)
                .build()
                .toString();
    }
}
