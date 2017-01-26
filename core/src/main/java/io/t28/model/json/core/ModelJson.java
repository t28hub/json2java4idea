package io.t28.model.json.core;

import com.google.common.io.Files;
import io.t28.model.json.core.json.JsonValue;
import io.t28.model.json.core.parser.JacksonParser;
import io.t28.model.json.core.parser.JsonParser;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class ModelJson {
    public static void main(String[] args) throws Exception {
        final JsonParser parser = new JacksonParser();

        final File emailsFile = new File("core/src/main/resources/emails.json");
        final String emailsJson = Files.toString(emailsFile, StandardCharsets.UTF_8);
        final JsonValue emails = parser.parse(emailsJson);
        System.out.println(emails);

        final File userFile = new File("core/src/main/resources/user.json");
        final String userJson = Files.toString(userFile, StandardCharsets.UTF_8);
        final JsonValue user = parser.parse(userJson);
        System.out.println(user);
    }
}
