package io.t28.model.json.core;

import com.google.common.io.Files;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.builder.BuilderType;
import io.t28.model.json.core.json.JsonValue;
import io.t28.model.json.core.naming.defaults.ClassNamingStrategy;
import io.t28.model.json.core.naming.defaults.FieldNamingStrategy;
import io.t28.model.json.core.naming.defaults.MethodNamingStrategy;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.defaults.PropertyNamingStrategy;
import io.t28.model.json.core.parser.JacksonParser;
import io.t28.model.json.core.parser.JsonParser;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class ModelJson {
    public static void main(String[] args) throws Exception {
        final JsonParser parser = new JacksonParser();

        final File file = new File("core/src/main/resources/user.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final JsonValue value = parser.parse(json);

        final Context context = Context.builder()
                .builderType(BuilderType.MODEL)
                .nameCase(NamingCase.LOWER_SNAKE_CASE)
                .classNameStrategy(new ClassNamingStrategy())
                .fieldNameStrategy(new FieldNamingStrategy())
                .methodNameStrategy(new MethodNamingStrategy())
                .propertyNameStrategy(new PropertyNamingStrategy())
                .build();

        final ClassFactory factory = new ClassFactory(context);
        final TypeSpec created = factory.create("User", value);
        System.out.println(created.toString());
    }
}
