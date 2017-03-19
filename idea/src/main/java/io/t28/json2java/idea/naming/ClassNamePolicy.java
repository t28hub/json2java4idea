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

/**
 *
 */
package io.t28.json2java.idea.naming;

import com.google.inject.Inject;
import com.intellij.psi.PsiNameHelper;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.Nonnull;

public class ClassNamePolicy implements NamePolicy {
    private final PsiNameHelper nameHelper;
    private final String prefix;
    private final String suffix;

    @Inject
    public ClassNamePolicy(@Nonnull PsiNameHelper nameHelper, @Nonnull String prefix, @Nonnull String suffix) {
        this.nameHelper = nameHelper;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final StringBuilder builder = new StringBuilder();
        builder.append(prefix)
                .append(DefaultNamePolicy.CLASS.convert(name, type))
                .append(suffix);
        final String className = builder.toString();
        if (!nameHelper.isQualifiedName(className)) {
            throw new IllegalArgumentException("Cannot convert '" + name + "' to class name");
        }
        return className;
    }
}
