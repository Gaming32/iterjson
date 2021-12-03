package io.github.gaming32.iterjson;

import java.io.IOException;

public class ReaderTest {
    public static void main(String[] args) throws IOException {
        try (JsonReader reader = new JsonReader("test.json")) {
            System.out.println(reader.getRoot().read());
        }
    }
}
