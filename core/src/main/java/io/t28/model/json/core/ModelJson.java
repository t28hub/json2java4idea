package io.t28.model.json.core;

import com.google.common.io.Files;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.builder.BuilderType;
import io.t28.model.json.core.json.JsonValue;
import io.t28.model.json.core.naming.defaults.ClassNameStrategy;
import io.t28.model.json.core.naming.defaults.FieldNameStrategy;
import io.t28.model.json.core.naming.defaults.MethodNameStrategy;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.defaults.PropertyNameStrategy;
import io.t28.model.json.core.parser.JacksonParser;
import io.t28.model.json.core.parser.JsonParser;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class ModelJson {
    public static void main(String[] args) throws Exception {
        final JsonParser parser = new JacksonParser();

        final File file = new File("core/src/main/resources/repositories.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final JsonValue value = parser.parse(json);

        final Context context = Context.builder()
                .builderType(BuilderType.MODEL)
                .nameCase(NamingCase.LOWER_SNAKE_CASE)
                .classNameStrategy(new ClassNameStrategy())
                .fieldNameStrategy(new FieldNameStrategy())
                .methodNameStrategy(new MethodNameStrategy())
                .propertyNameStrategy(new PropertyNameStrategy())
                .build();

        final ClassFactory factory = new ClassFactory(context);
        final TypeSpec created = factory.create("User", value);
        System.out.println(created.toString());
    }
}
