package io.t28.json2java.idea.inject;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.intellij.psi.PsiNameHelper;
import io.t28.json2java.core.Configuration;
import io.t28.json2java.core.JavaConverter;
import io.t28.json2java.core.naming.NamePolicy;
import io.t28.json2java.idea.naming.ClassNamePolicy;
import io.t28.json2java.idea.settings.Json2JavaSettings;

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
