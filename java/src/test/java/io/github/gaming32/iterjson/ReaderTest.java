package io.github.gaming32.iterjson;

import java.io.IOException;
import java.io.InputStreamReader;

public class ReaderTest {
    public static void main(String[] args) throws IOException {
        try (JsonReader reader = new JsonReader(
            new InputStreamReader(
                ReaderTest.class.getResourceAsStream("/test.json")
            )
        )) {
            System.out.println(reader.getRoot().read());
        }
    }
}
