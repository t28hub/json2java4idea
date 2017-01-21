package io.t28.jsonmodel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        final File file = new File("jsoon-core/src/main/resources/issue.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final JsonNode node = new ObjectMapper().readTree(json);
        final JsonElement element = JsonElement.create(node);
        final ClassBuilder builder = new ModelBuilder("Example");
        final TypeSpec typeSpec = builder.build(element);
        System.out.println(typeSpec);
    }
}
