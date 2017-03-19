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

package io.t28.json2java.idea.inject;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.intellij.psi.PsiNameHelper;
import io.t28.json2java.core.Configuration;
import io.t28.json2java.core.JavaConverter;
import io.t28.json2java.core.naming.NamePolicy;
import io.t28.json2java.idea.naming.ClassNamePolicy;
import io.t28.json2java.idea.setting.Json2JavaSettings;

import javax.annotation.Nonnull;

public class JavaConverterFactory {
    private final Provider<PsiNameHelper> nameHelperProvider;
    private final Provider<NamePolicy> fieldNamePolicyProvider;
    private final Provider<NamePolicy> methodNamePolicyProvider;
    private final Provider<NamePolicy> parameterNamePolicyProvider;

    @Inject
    public JavaConverterFactory(@Nonnull Provider<PsiNameHelper> nameHelperProvider,
                                @Nonnull @Named("FieldName") Provider<NamePolicy> fieldNamePolicyProvider,
                                @Nonnull @Named("MethodName") Provider<NamePolicy> methodNamePolicyProvider,
                                @Nonnull @Named("ParameterName") Provider<NamePolicy> parameterNamePolicyProvider) {
        this.nameHelperProvider = nameHelperProvider;
        this.fieldNamePolicyProvider = fieldNamePolicyProvider;
        this.methodNamePolicyProvider = methodNamePolicyProvider;
        this.parameterNamePolicyProvider = parameterNamePolicyProvider;
    }

    @Nonnull
    public JavaConverter create(@Nonnull Json2JavaSettings settings) {
        final Configuration configuration = Configuration.builder()
                .style(settings.getStyle())
                .classNamePolicy(new ClassNamePolicy(
                        nameHelperProvider.get(),
                        settings.getClassNamePrefix(),
                        settings.getClassNameSuffix())
                )
                .fieldNamePolicy(fieldNamePolicyProvider.get())
                .methodNamePolicy(methodNamePolicyProvider.get())
                .parameterNamePolicy(parameterNamePolicyProvider.get())
                .build();
        return new JavaConverter(configuration);
    }
}
