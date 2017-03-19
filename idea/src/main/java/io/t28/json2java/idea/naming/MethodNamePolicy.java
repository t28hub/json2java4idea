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
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.intellij.codeInsight.generation.GenerateMembersUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiType;
import com.intellij.util.IncorrectOperationException;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import io.t28.json2java.core.naming.NamePolicy;
import io.t28.json2java.idea.util.PsiTypeConverter;

import javax.annotation.Nonnull;

public class MethodNamePolicy implements NamePolicy {
    private final Project project;
    private final PsiTypeConverter typeConverter;

    @Inject
    public MethodNamePolicy(@Nonnull Project project, @Nonnull PsiTypeConverter typeConverter) {
        this.project = Preconditions.checkNotNull(project);
        this.typeConverter = Preconditions.checkNotNull(typeConverter);
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String variableName = DefaultNamePolicy.format(name, CaseFormat.UPPER_CAMEL);
        if (Strings.isNullOrEmpty(variableName)) {
            throw new IllegalArgumentException("Cannot convert '" + name + "' to a method name");
        }

        try {
            final PsiType psiType = typeConverter.apply(type);
            return GenerateMembersUtil.suggestGetterName(variableName, psiType, project);
        } catch (IncorrectOperationException e) {
            throw new IllegalArgumentException("Cannot convert '" + name + "' to a method name", e);
        }
    }
}
