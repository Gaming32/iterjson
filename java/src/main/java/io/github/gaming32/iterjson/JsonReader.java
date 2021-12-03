package io.github.gaming32.iterjson;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import io.github.gaming32.iterjson.values.JsonValue;

public final class JsonReader implements AutoCloseable {
    private final DataReader reader;
    private JsonValue<Object> root;

    public JsonReader(FileDescriptor fd) {
        this(new DataReader(fd));
    }

    public JsonReader(String fileName) throws FileNotFoundException {
        this(new DataReader(fileName));
    }

    public JsonReader(File file) throws FileNotFoundException {
        this(new DataReader(file));
    }

    public JsonReader(Reader reader) {
        this(new DataReader(reader));
    }

    public JsonReader(DataReader reader) {
        this.reader = reader;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    public JsonValue<Object> getRoot() throws IOException {
        if (root == null) {
            root = reader.readValue();
        }
        return root;
    }
}
