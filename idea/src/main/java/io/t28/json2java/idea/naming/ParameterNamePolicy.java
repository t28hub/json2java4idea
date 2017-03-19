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

package io.t28.json2java.idea.naming;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.VariableKind;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.Nonnull;

public class ParameterNamePolicy implements NamePolicy {
    private final JavaCodeStyleManager codeStyleManager;

    @Inject
    public ParameterNamePolicy(@Nonnull JavaCodeStyleManager codeStyleManager) {
        this.codeStyleManager = codeStyleManager;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String propertyName = DefaultNamePolicy.format(name, CaseFormat.LOWER_CAMEL);
        final String parameterName = codeStyleManager.propertyNameToVariableName(propertyName, VariableKind.PARAMETER);
        if (Strings.isNullOrEmpty(parameterName)) {
            throw new IllegalArgumentException("Cannot convert '" + name + "' to a parameter name");
        }
        return parameterName;
    }
}
