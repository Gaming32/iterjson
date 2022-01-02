package io.github.gaming32.iterjson;

import java.io.IOException;
import java.io.InputStreamReader;

import io.github.gaming32.iterjson.values.ArrayValue;
import io.github.gaming32.iterjson.values.JsonValue;

public class ReaderTest {
    public static void main(String[] args) throws IOException {
        try (JsonReader reader = new JsonReader(
            new InputStreamReader(
                ReaderTest.class.getResourceAsStream("/test.json")
            )
        )) {
            // System.out.println(reader.getRoot().read());
            for (JsonValue<Object> val : (ArrayValue)reader.getRoot()) {
                System.out.println(val.getValue());
            }
        }
    }
}
