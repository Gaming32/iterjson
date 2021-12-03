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
import io.github.gaming32.iterjson.values.StringValue;

public final class DataReader implements AutoCloseable {
    @SuppressWarnings("rawtypes")
    @FunctionalInterface
    private static interface JsonValueConstructor {
        public JsonValue construct(char first, DataReader reader);
    }

    private static final Map<Character, JsonValueConstructor> CHAR_TYPE_MAP = new HashMap<Character, JsonValueConstructor>() {{
        put('"', StringValue::new);

        put('n', ConstantValue::new);
        put('t', ConstantValue::new);
        put('f', ConstantValue::new);
    }};

    private static final List<Character> WHITESPACE = Arrays.asList(' ', '\r', '\n', '\t');

    private final Reader reader;
    private char buffer;

    public DataReader(FileDescriptor fd) {
        this(new FileReader(fd));
    }

    public DataReader(String fileName) throws FileNotFoundException {
        this(new FileReader(fileName));
    }

    public DataReader(File file) throws FileNotFoundException {
        this(new FileReader(file));
    }

    public DataReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    public Reader getReader() {
        return reader;
    }

    private String readAsString(int n) throws IOException {
        if (n == 1) { // Common case
            int c = reader.read();
            if (n == -1) {
                throw new JsonFormatException("EOF");
            }
            return Character.toString((char)c);
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
