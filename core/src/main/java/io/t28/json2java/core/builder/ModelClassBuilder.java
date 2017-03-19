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

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class ModelClassBuilder extends ClassBuilder {
    public ModelClassBuilder(@Nonnull NamePolicy fieldNameStrategy,
                             @Nonnull NamePolicy methodNameStrategy,
                             @Nonnull NamePolicy parameterNameStrategy) {
        super(fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
    }

    @Nonnull
    @Override
    protected List<FieldSpec> buildFields() {
        return getProperties()
                .entrySet()
                .stream()
                .map(property -> {
                    final String name = property.getKey();
                    final TypeName type = property.getValue();
                    final String fieldName = fieldNamePolicy.convert(name, type);
                    return FieldSpec.builder(type, fieldName)
                            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    protected List<MethodSpec> buildMethods() {
        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        final ImmutableList.Builder<MethodSpec> builder = ImmutableList.builder();
        getProperties().entrySet().forEach(property -> {
            final String name = property.getKey();
            final TypeName type = property.getValue();
            final String fieldName = fieldNamePolicy.convert(name, type);
            final String methodName = methodNamePolicy.convert(name, type);
            builder.add(MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(type)
                    .addStatement("return $L", fieldName)
                    .build());

            final String propertyName = parameterNamePolicy.convert(name, type);
            constructorBuilder.addParameter(ParameterSpec.builder(type, propertyName)
                    .build())
                    .addStatement("this.$L = $L", fieldName, propertyName);
        });
        builder.add(constructorBuilder.build());
        return builder.build();
    }
}
