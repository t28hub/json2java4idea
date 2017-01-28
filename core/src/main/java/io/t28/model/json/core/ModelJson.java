package io.t28.model.json.core;

import com.google.common.io.Files;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.builder.ClassBuilder;
import io.t28.model.json.core.json.JsonValue;
import io.t28.model.json.core.naming.DefaultNamingStrategy;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingStrategy;
import io.t28.model.json.core.parser.JacksonParser;
import io.t28.model.json.core.parser.JsonParser;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class ModelJson {
    public static void main(String[] args) throws Exception {
        final JsonParser parser = new JacksonParser();

        final File file = new File("core/src/main/resources/repositories.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final JsonValue value = parser.parse(json);

        final ClassFactory factory = new ClassFactory(
                ClassBuilder.Type.MODEL,
                NamingCase.LOWER_SNAKE_CASE,
                new DefaultNamingStrategy(NamingCase.UPPER_CAMEL_CASE, null, null),
                new DefaultNamingStrategy(NamingCase.LOWER_CAMEL_CASE, "m", null),
                new NamingStrategy() {
                    @Nonnull
                    @Override
                    public String transform(@Nonnull TypeName type, @Nonnull String name, @Nonnull NamingCase nameCase) {
                        if (type.equals(TypeName.BOOLEAN)) {
                            return "is" + nameCase.to(NamingCase.UPPER_CAMEL_CASE, name);
                        }
                        return "get" + nameCase.to(NamingCase.UPPER_CAMEL_CASE, name);
                    }
                }
        );
        final TypeSpec created = factory.create("User", value);
        System.out.println(created.toString());
    }
}
