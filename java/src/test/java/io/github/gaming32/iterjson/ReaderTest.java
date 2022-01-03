package io.github.gaming32.iterjson;

import java.io.IOException;
import java.io.InputStreamReader;

import io.github.gaming32.iterjson.values.ObjectValue;

public class ReaderTest {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 2; i++) {
            try (JsonReader reader = new JsonReader(
                new InputStreamReader(
                    ReaderTest.class.getResourceAsStream("/test.json")
                )
            )) {
                System.out.println(((ObjectValue)reader.getRoot()).read(i == 0));
            }
        }
    }
}
