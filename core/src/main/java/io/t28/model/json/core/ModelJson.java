package io.t28.model.json.core;

import com.google.common.io.Files;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.builder.ClassBuilder;
import io.t28.model.json.core.builder.ModelClassBuilder;
import io.t28.model.json.core.json.JsonObject;
import io.t28.model.json.core.json.JsonValue;
import io.t28.model.json.core.parser.JacksonParser;
import io.t28.model.json.core.parser.JsonParser;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class ModelJson {
    public static void main(String[] args) throws Exception {
        final JsonParser parser = new JacksonParser();

        final File userFile = new File("core/src/main/resources/user.json");
        final String userJson = Files.toString(userFile, StandardCharsets.UTF_8);
        final JsonValue user = parser.parse(userJson);
        if (!user.isObject()) {
            throw new RuntimeException();
        }

        final TypeSpec generated = generate(user.asObject(), "User", Modifier.PUBLIC);
        System.out.println(generated);
    }

    private static TypeSpec generate(JsonObject object, String className, Modifier... modifiers) {
        final ClassBuilder builder = new ModelClassBuilder(className);
        builder.addModifiers(modifiers);
        object.stream().forEach(child -> {
            final String name = child.getKey();
            final JsonValue value = child.getValue();
            if (value.isObject()) {
                final TypeSpec enclosedClass = generate(value.asObject(), name, Modifier.PUBLIC, Modifier.STATIC);
                builder.addEnclosedClass(enclosedClass);

                final TypeName type = ClassName.bestGuess(enclosedClass.name);
                builder.addProperty(name, type);
                return;
            }

            if (value.isArray()) {

                return;
            }

            builder.addProperty(name, value.getType());
        });
        return builder.build();
    }
}
