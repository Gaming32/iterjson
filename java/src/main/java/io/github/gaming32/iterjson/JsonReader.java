package io.github.gaming32.iterjson;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.gaming32.iterjson.values.ConstantValue;
import io.github.gaming32.iterjson.values.JsonValue;

public final class JsonReader implements AutoCloseable {
    @SuppressWarnings("rawtypes")
    @FunctionalInterface
    private static interface JsonValueConstructor {
        public JsonValue construct(char first, JsonReader reader);
    }

    private static final Map<Character, JsonValueConstructor> CHAR_TYPE_MAP = new HashMap<Character, JsonValueConstructor>() {{
        put('n', ConstantValue::new);
        put('t', ConstantValue::new);
        put('f', ConstantValue::new);
    }};

    private static final List<Character> WHITESPACE = Arrays.asList(' ', '\r', '\n', '\t');

    private Reader reader;
    private JsonValue<Object> root;
    private char buffer;

    public JsonReader(FileDescriptor fd) {
        this(new FileReader(fd));
    }

    public JsonReader(String fileName) throws FileNotFoundException {
        this(new FileReader(fileName));
    }

    public JsonReader(File file) throws FileNotFoundException {
        this(new FileReader(file));
    }

    public JsonReader(Reader reader) {
        this.reader = reader;
    }

    public JsonValue<Object> getRoot() throws IOException {
        if (root == null) {
            root = readValue();
        }
        return root;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    private String readAsString(int n) throws IOException {
        if (n == 1) {
            int c = reader.read();
            if (n == -1) {
                throw new JsonFormatException("EOF");
            }
            return Character.toString((char)c); // Common case
        }
        char[] buff = new char[n];
        n = reader.read(buff, 0, n);
        if (n == -1) {
            throw new JsonFormatException("EOF");
        }
        return new String(buff, 0, n);
    }

    public String readString(int n) throws IOException {
        if (this.buffer != '\0') {
            char buffer = this.buffer;
            this.buffer = '\0';
            if (n == 1) return Character.toString(buffer);
            return buffer + readAsString(n - 1);
        }
        return readAsString(n);
    }

    public char readChar() throws IOException {
        if (this.buffer != '\0') {
            char buffer = this.buffer;
            this.buffer = '\0';
            return buffer;
        }
        int result = reader.read();
        if (result == -1) {
            throw new JsonFormatException("EOF");
        }
        return (char)result;
    }

    public JsonValue<Object> readValue() throws IOException {
        return readValue(readPastWhitespace());
    }

    @SuppressWarnings("unchecked")
    public JsonValue<Object> readValue(char c) {
        JsonValueConstructor constructor = CHAR_TYPE_MAP.get(c);
        if (constructor == null) {
            JsonFormatException.throwOnCharacter(c);
        }
        return (JsonValue<Object>)constructor.construct((char)c, this);
    }

    public char readPastWhitespace() throws IOException{
        char c;
        while (WHITESPACE.contains(c = readChar()));
        return c;
    }
}
